package com.cs307.project.service;

import com.cs307.project.entity.*;

import java.util.Date;
import java.util.List;

public interface IService {
    void stockIn(StockIn stockIn);

    void placeOrder(PlaceOrder placeOrder);

    void updateOrder(String contractNum, String productModel, String salesmanNum, int quantity, Date estimatedDeliveryDate, Date lodgementDate);

    void deleteOrder(String contract, String salesman, int seq);

    List<StaffCount> getAllStaffCount();

    Integer getOrderCount();

    Integer getContractCount();

    Integer getNeverSoldProductCount();

    List<AvgStockByCenter> getAvgStockByCenter();

    FavoriteModel getFavoriteProductModel();

    List<ProductStock> getProductByNumber(String number);

    Contract getContractInfo(String contract_number);

    PlaceOrder getOrder(String sorting, String key, String page);
}