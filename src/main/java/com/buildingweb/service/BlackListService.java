package com.buildingweb.service;

public interface BlackListService {
    public void addToBlackList(String token);

    public Boolean isBlackList(String token);
}