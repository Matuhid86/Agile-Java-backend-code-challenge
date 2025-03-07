package com.agiletv.users.domain.model;

public interface IUser {
    String getUsername();
    String getName();
    String getEmail();
    Gender getGender();
    ILocation getLocation();
    String getUrlPhoto();
}
