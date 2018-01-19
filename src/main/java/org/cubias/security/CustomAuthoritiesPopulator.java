package org.cubias.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.cubias.entities.FowUser;
import org.cubias.repositories.FowUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthoritiesPopulator implements LdapAuthoritiesPopulator {

	private static Logger log = Logger.getLogger(CustomAuthoritiesPopulator.class);

	@Autowired
	private FowUserRepository fowUserRepository;

	@Override
	public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData,
			String username) {
		return getGrantedAuthorities(username);
	}

	private List<GrantedAuthority> getGrantedAuthorities(String username) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		try {
			FowUser fowUser = fowUserRepository.findOneByUseAdUser(username);
			if (fowUser != null) {
				authorities.add(new SimpleGrantedAuthority(fowUser.getUseRole().getRolName()));
			}			
		} catch (Exception e) {
			log.error("An error has occurred when the app tryed to get the roles for this user. " + username);
			e.printStackTrace();
		}
		return authorities;
	}

}
