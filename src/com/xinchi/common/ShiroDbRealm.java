package com.xinchi.common;

import java.util.Collection;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;


public class ShiroDbRealm extends AuthorizingRealm {
	// 实现用户的认证
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {
		// User user = userService.get(authcToken.getUsername());
		// ShiroUser shiroUser = new ShiroUser(user.getId(), user.getUsername(),
		// user);
		// code_snippet_id="239402" snippet_file_name="blog_20140316_8_5493693"
		// name="code" class="html">
		return null;// new SimpleAuthenticationInfo(shiroUser,
					// user.getPassword(),ByteSource.Util.bytes(salt),
					// getName());
	}

	// 实现用户的鉴权
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Collection<?> collection = principals.fromRealm(getName());
		if (CollectionUtils.isEmpty(collection)) {
			return null;
		}
		// ShiroUser shiroUser = (ShiroUser) collection.iterator().next();
		//
		// List<UserRole> userRoles = userRoleService.find(shiroUser.getId());
		// List<OrganizationRole> organizationRoles = organizationRoleService
		// .find(shiroUser.getUser().getOrganization().getId());

		// SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		// info.addStringPermissions(makePermissions(userRoles,
		// organizationRoles, shiroUser));

		return null;// info;
	}
}
