package com.example.keycloakuserstore.representations;

import com.example.keycloakuserstore.dao.UserDAO;
import com.example.keycloakuserstore.models.User;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class UserRepresentation extends AbstractUserAdapterFederatedStorage {
    private User userEntity;
    private UserDAO userDAO;
	Logger logger = Logger.getLogger(this.getClass().getName());
    public UserRepresentation(KeycloakSession session,
                              RealmModel realm,
                              ComponentModel storageProviderModel,
                              User userEntity,
                              UserDAO userDAO) {
        super(session, realm, storageProviderModel);
        this.userEntity = userEntity;
        this.userDAO = userDAO;
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    @Override
    public void setUsername(String username) {
        userEntity.setUsername(username);
        userEntity = userDAO.updateUser(userEntity);
    }

    @Override
    public void setEmail(String email) {
        userEntity.setEmail(email);
        userEntity = userDAO.updateUser(userEntity);
    }

    @Override
    public String getEmail() {
        return userEntity.getEmail();
    }

    @Override
    public void setSingleAttribute(String name, String value) {
        if (name.equals("phone")) {
            userEntity.setPhone(value);
        } else if (name.equals("userlevel")) {
            userEntity.setUserlevel(Integer.parseInt(value));
        } else {
            super.setSingleAttribute(name, value);
        }
    }

    @Override
    public void removeAttribute(String name) {
        if (name.equals("phone")) {
            userEntity.setPhone(null);
        } else if (name.equals("userlevel")) {
            userEntity.setUserlevel(null);
        } else {
            super.removeAttribute(name);
        }
		
        userEntity = userDAO.updateUser(userEntity);
    }

    @Override
    public void setAttribute(String name, List<String> values) {
        if (name.equals("phone")) {
            userEntity.setPhone(values.get(0));
        } else if (name.equals("userlevel")) {
            userEntity.setUserlevel(Integer.parseInt(values.get(0)));
        } else {
            super.setAttribute(name, values);
        }
        userEntity = userDAO.updateUser(userEntity);
    }

    @Override
    public String getFirstAttribute(String name) {
        if (name.equals("phone")) {
            return userEntity.getPhone();
        } else if (name.equals("userlevel")) {
            return userEntity.getUserlevel().toString();
        } else {
            return super.getFirstAttribute(name);
        }
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        Map<String, List<String>> attrs = super.getAttributes();
        MultivaluedHashMap<String, String> all = new MultivaluedHashMap<>();
        all.putAll(attrs);
        all.add("phone", userEntity.getPhone());
		all.add("userlevel", userEntity.getUserlevel().toString());
        return all;
    }

    @Override
    public List<String> getAttribute(String name) {
        if (name.equals("phone")) {
            List<String> phone = new LinkedList<>();
            phone.add(userEntity.getPhone());
            return phone;
        } else if (name.equals("userlevel")) {
            List<String> userlevel = new LinkedList<>();
            userlevel.add(userEntity.getUserlevel().toString());
            return userlevel;
        }else {
            return super.getAttribute(name);
        }
    }

    @Override
    public String getId() {
        return StorageId.keycloakId(storageProviderModel, userEntity.getId().toString());
    }

    public String getPassword() {
		logger.info("get entity password");
		System.out.println("Hello");
		System.out.println(userEntity.getPassword()); 
        return userEntity.getPassword();
    }

    public void setPassword(String password) {
        userEntity.setPassword(password);
        userEntity = userDAO.updateUser(userEntity);
    }
}
