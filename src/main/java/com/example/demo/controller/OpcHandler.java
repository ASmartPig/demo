package com.example.demo.controller;

import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.*;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.Executors;

@Service
public class OpcHandler {

    public  void read(){
        ConnectionInformation ci = new ConnectionInformation();
        ci.setHost("10.2.1.106");
        ci.setDomain("");
        ci.setUser("OPCUser");
        ci.setPassword("root");
        ci.setClsid("F8582CF2-88FB-11D0-B850-00C0F0104305");

        Server server = new Server(ci, Executors.newSingleThreadScheduledExecutor());
        try {
            server.connect();
        } catch (AlreadyConnectedException e) {
            e.printStackTrace();
        } catch (JIException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        Group group = null;
        try {
            group = server.addGroup();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (NotConnectedException e) {
            e.printStackTrace();
        } catch (JIException e) {
            e.printStackTrace();
        } catch (DuplicateGroupException e) {
            e.printStackTrace();
        }

        Item item = null;
        try {
            item = group.addItem("group_two.item_one");
        } catch (JIException e) {
            e.printStackTrace();
        } catch (AddFailedException e) {
            e.printStackTrace();
        }

        Map<String, Item> items = null;
        try {
            items = group.addItems(
                    "group_two.item_two");
        } catch (JIException e) {
            e.printStackTrace();
        } catch (AddFailedException e) {
            e.printStackTrace();
        }

        dumpItem(item);

        for (Map.Entry<String, Item> temp : items.entrySet()) {
            dumpItem(temp.getValue());
        }

        server.dispose();
    }

    private static void dumpItem(Item item) {
        try {
            System.out.println("[" + (++count) + "],ItemName:[" + item.getId()
                    + "],value:" + item.read(false).getValue());
        } catch (JIException e) {
            e.printStackTrace();
        }
    }


    private static int count;

}
