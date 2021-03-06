package org.cubias.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.SpringSecurityLdapTemplate;
import org.springframework.security.ldap.authentication.AbstractLdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Specialized LDAP authentication provider which uses Active Directory
 * configuration conventions. Also comes with LdapAuthoritiesPopulator support
 * for external Role loading.
 * <p>
 * It will authenticate using the Active Directory <a href=
 * "http://msdn.microsoft.com/en-us/library/ms680857%28VS.85%29.aspx">{@code userPrincipalName}</a>
 * (in the form {@code username@domain}). If the username does not already end
 * with the domain name, the {@code userPrincipalName} will be built by
 * appending the configured domain name to the username supplied in the
 * authentication request. If no domain name is configured, it is assumed that
 * the username will always contain the domain name.
 * <p>
 * The user authorities are obtained from the data contained in the
 * {@code memberOf} attribute.
 *
 * <h3>Active Directory Sub-Error Codes</h3>
 *
 * When an authentication fails, resulting in a standard LDAP 49 error code,
 * Active Directory also supplies its own sub-error codes within the error
 * message. These will be used to provide additional log information on why an
 * authentication has failed. Typical examples are
 *
 * <ul>
 * <li>525 - user not found</li>
 * <li>52e - invalid credentials</li>
 * <li>530 - not permitted to logon at this time</li>
 * <li>532 - password expired</li>
 * <li>533 - account disabled</li>
 * <li>701 - account expired</li>
 * <li>773 - user must reset password</li>
 * <li>775 - account locked</li>
 * </ul>
 *
 * If you set the {@link #setConvertSubErrorCodesToExceptions(boolean)
 * convertSubErrorCodesToExceptions} property to {@code true}, the codes will
 * also be used to control the exception raised.
 *
 * @author Luke Taylor
 * @author Srimanta Roy (harrydoors@yahoo.com)
 * @since 3.1
 */
public final class CustomActiveDirectoryLdapAuthenticationProvider extends AbstractLdapAuthenticationProvider {
	private static final Pattern SUB_ERROR_CODE = Pattern.compile(".*\\s([0-9a-f]{3,4}).*");

	// Error codes
	private static final int USERNAME_NOT_FOUND = 0x525;
	private static final int INVALID_PASSWORD = 0x52e;
	private static final int NOT_PERMITTED = 0x530;
	private static final int PASSWORD_EXPIRED = 0x532;
	private static final int ACCOUNT_DISABLED = 0x533;
	private static final int ACCOUNT_EXPIRED = 0x701;
	private static final int PASSWORD_NEEDS_RESET = 0x773;
	private static final int ACCOUNT_LOCKED = 0x775;

	private final String domain;
	private final String rootDn;
	private final String url;
	private boolean convertSubErrorCodesToExceptions;
	private LdapAuthoritiesPopulator externalAuthoritiesPopulator;

	// McCubo was here
	// @Autowired
	// private CustomAuthoritiesPopulator customAuthoritiesPopulator;

	/**
	 * @param domain
	 *            the domain for which authentication should take place
	 */
	// public ActiveDirectoryLdapAuthenticationProvider(String domain) {
	// this (domain, null);
	// }

	/**
	 * @param domain
	 *            the domain name
	 * @param url
	 *            an LDAP url (or multiple URLs)
	 */
	public CustomActiveDirectoryLdapAuthenticationProvider(String domain, String url) {
		Assert.isTrue(StringUtils.hasText(domain) || StringUtils.hasText(url), "Domain and url cannot both be empty");
		this.domain = StringUtils.hasText(domain) ? domain.toLowerCase() : null;
		this.url = StringUtils.hasText(url) ? url : null;
		rootDn = this.domain == null ? null : rootDnFromDomain(this.domain);

		// System.out.println("Dominio: "+this.domain);
		// System.out.println("URL: "+this.url);

	}

	/**
	 * @param domain
	 *            the domain name
	 * @param url
	 *            an LDAP url (or multiple URLs)
	 * @param externalAuthoritiesPopulator
	 *            - to load Collection<GrantedAuthority> from external source
	 */
	public CustomActiveDirectoryLdapAuthenticationProvider(String domain, String url,
			LdapAuthoritiesPopulator externalAuthoritiesPopulator) {
		// this(domain, url);
		Assert.isTrue(StringUtils.hasText(domain) || StringUtils.hasText(url), "Domain and url cannot both be empty");
		this.domain = StringUtils.hasText(domain) ? domain.toLowerCase() : null;
		this.url = StringUtils.hasText(url) ? url : null;
		rootDn = this.domain == null ? null : rootDnFromDomain(this.domain);
		this.externalAuthoritiesPopulator = externalAuthoritiesPopulator;
	}

	@Override
	protected DirContextOperations doAuthentication(UsernamePasswordAuthenticationToken auth) {
		String username = auth.getName();
		String password = (String) auth.getCredentials();

		LdapContext ctx = bindAsUser(username, password);

		try {
			DirContextOperations dco;

			//
			dco = searchForUser(ctx, username);

			return dco;
			// return new DirContextOperations();

		} catch (Exception e) {
			// logger.error("Failed to locate directory entry for authenticated user: " +
			// username, e);
			throw badCredentials();
		} finally {
			LdapUtils.closeContext(ctx);
		}
	}

	/**
	 * Creates the user authority list from the values of the {@code memberOf}
	 * attribute obtained from the user's Active Directory entry.
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Collection<? extends GrantedAuthority> loadUserAuthorities(DirContextOperations userData, String username,
			String password) {
		if (externalAuthoritiesPopulator == null) {
			throw new BadCredentialsException("You don't have permission to use this tool...");
		} else {
			List<GrantedAuthority> externalAuthoritylist = (List<GrantedAuthority>) externalAuthoritiesPopulator
					.getGrantedAuthorities(userData, username);
			boolean extContains = externalAuthoritylist != null && externalAuthoritylist.size() > 0;
			if (!extContains) {
				throw permissionError();
			}
			ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(externalAuthoritylist.size());
			authorities.addAll(externalAuthoritylist);
			return authorities;
		}
	}

	protected Collection<? extends GrantedAuthority> loadUserAuthoritiesFromMemberOf(DirContextOperations userData,
			String username, String password) {
		String[] groups = userData.getStringAttributes("memberOf");

		if (groups == null) {
			// logger.debug("No values for 'memberOf' attribute.");

			return AuthorityUtils.NO_AUTHORITIES;
		}

		/*
		 * if (logger.isDebugEnabled()) { logger.debug("'memberOf' attribute values: " +
		 * Arrays.asList(groups)); }
		 */

		ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(groups.length);

		for (String group : groups) {
			authorities.add(new SimpleGrantedAuthority(new DistinguishedName(group).removeLast().getValue()));
		}

		return authorities;
	}

	private LdapContext bindAsUser(String username, String password) {
		// TODO. add DNS lookup based on domain
		final String bindUrl = url;

		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		String bindPrincipal = createBindPrincipal(username);
		env.put(Context.SECURITY_PRINCIPAL, bindPrincipal);
		env.put(Context.PROVIDER_URL, bindUrl);
		env.put(Context.SECURITY_CREDENTIALS, password);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.REFERRAL, "ignore");
		// env.put(Context.REFERRAL, "follow");

		try {
			return new InitialLdapContext(env, null);
		} catch (NamingException e) {
			if ((e instanceof AuthenticationException) || (e instanceof OperationNotSupportedException)) {
				handleBindException(bindPrincipal, e);
				throw badCredentials();
			} else {
				throw LdapUtils.convertLdapException(e);
			}
		}
	}

	void handleBindException(String bindPrincipal, NamingException exception) {
		/*
		 * if (logger.isDebugEnabled()) { logger.debug("Authentication for " +
		 * bindPrincipal + " failed:" + exception); }
		 */

		int subErrorCode = parseSubErrorCode(exception.getMessage());

		if (subErrorCode > 0) {
			// logger.info("Active Directory authentication failed: " +
			// subCodeToLogMessage(subErrorCode));

			if (convertSubErrorCodesToExceptions) {
				raiseExceptionForErrorCode(subErrorCode);
			}
		} else {
			// logger.debug("Failed to locate AD-specific sub-error code in message");
		}
	}

	int parseSubErrorCode(String message) {
		Matcher m = SUB_ERROR_CODE.matcher(message);

		if (m.matches()) {
			return Integer.parseInt(m.group(1), 16);
		}

		return -1;
	}

	void raiseExceptionForErrorCode(int code) {
		switch (code) {
		case PASSWORD_EXPIRED:
			throw new CredentialsExpiredException(messages.getMessage("LdapAuthenticationProvider.credentialsExpired",
					"User credentials have expired"));
		case ACCOUNT_DISABLED:
			throw new DisabledException(messages.getMessage("LdapAuthenticationProvider.disabled", "User is disabled"));
		case ACCOUNT_EXPIRED:
			throw new AccountExpiredException(
					messages.getMessage("LdapAuthenticationProvider.expired", "User account has expired"));
		case ACCOUNT_LOCKED:
			throw new LockedException(
					messages.getMessage("LdapAuthenticationProvider.locked", "User account is locked"));
		case NOT_PERMITTED:
			throw new DisabledException("User not permitted to logon on this application");

		}
	}

	String subCodeToLogMessage(int code) {
		switch (code) {
		case USERNAME_NOT_FOUND:
			return "User was not found in directory";
		case INVALID_PASSWORD:
			return "Supplied password was invalid";
		case NOT_PERMITTED:
			return "User not permitted to logon at this time";
		case PASSWORD_EXPIRED:
			return "Password has expired";
		case ACCOUNT_DISABLED:
			return "Account is disabled";
		case ACCOUNT_EXPIRED:
			return "Account expired";
		case PASSWORD_NEEDS_RESET:
			return "User must reset password";
		case ACCOUNT_LOCKED:
			return "Account locked";
		}

		return "Unknown (error code " + Integer.toHexString(code) + ")";
	}

	private BadCredentialsException badCredentials() {
		return new BadCredentialsException(
				messages.getMessage("LdapAuthenticationProvider.badCredentials", "Bad credentials"));
	}

	private BadCredentialsException permissionError() {
		return new BadCredentialsException("You don't have permissions to enter in this tool");
	}

	private DirContextOperations searchForUser(LdapContext ctx, String username) throws NamingException {
		SearchControls searchCtls = new SearchControls();
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String searchFilter = "(&(samaccountname=" + username + "))";
		final String bindPrincipal = createBindPrincipal(username);
		String searchRoot = rootDn != null ? rootDn : searchRootFromPrincipal(bindPrincipal);
		DirContextOperations dco = CustomSpringSecurityLdapTemplate.searchForSingleEntryInternal(ctx, searchCtls,
				searchRoot, searchFilter, new Object[] { bindPrincipal });
		return dco;
	}

	private String searchRootFromPrincipal(String bindPrincipal) {
		return rootDnFromDomain(bindPrincipal.substring(bindPrincipal.lastIndexOf('@') + 1, bindPrincipal.length()));
	}

	private String rootDnFromDomain(String domain) {
		String[] tokens = StringUtils.tokenizeToStringArray(domain, ".");
		StringBuilder root = new StringBuilder();

		for (String token : tokens) {
			if (root.length() > 0) {
				root.append(',');
			}
			root.append("dc=").append(token);
		}

		return root.toString();
	}

	String createBindPrincipal(String username) {
		if (domain == null || username.toLowerCase().endsWith(domain)) {
			return username;
		}

		return username + "@" + domain;
	}

	/**
	 * By default, a failed authentication (LDAP error 49) will result in a
	 * {@code BadCredentialsException}.
	 * <p>
	 * If this property is set to {@code true}, the exception message from a failed
	 * bind attempt will be parsed for the AD-specific error code and a
	 * {@link CredentialsExpiredException}, {@link DisabledException},
	 * {@link AccountExpiredException} or {@link LockedException} will be thrown for
	 * the corresponding codes. All other codes will result in the default
	 * {@code BadCredentialsException}.
	 *
	 * @param convertSubErrorCodesToExceptions
	 *            {@code true} to raise an exception based on the AD error code.
	 */
	public void setConvertSubErrorCodesToExceptions(boolean convertSubErrorCodesToExceptions) {
		this.convertSubErrorCodesToExceptions = convertSubErrorCodesToExceptions;
	}

}