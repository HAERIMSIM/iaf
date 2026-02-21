package com.iaf.mapper;

import com.iaf.model.Client;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClientMapper {
    List<Client> selectClientList();
    Client selectClientById(Long clientId);
    List<String> selectCategoryListByClientId(Long clientId);
}