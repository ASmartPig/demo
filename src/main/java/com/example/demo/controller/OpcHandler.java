package com.example.demo.controller;

import com.example.demo.enums.CEMS;
import com.google.common.collect.Maps;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIArray;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.*;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

@Service
public class OpcHandler {

    public Map<String,Double> read() {
        Map<String,Double> map = Maps.newHashMap();

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

        Map<String, Item> items = null;
        try {
             items = group.addItems(CEMS.CEMS_in_NOX.getStr(),CEMS.CEMS_in_SO2.getStr(),
                    CEMS.CEMS_in_flux.getStr(),CEMS.CEMS_in_O2.getStr(),CEMS.CEMS_in_temp.getStr());
        } catch (JIException e) {
            e.printStackTrace();
        } catch (AddFailedException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, Item> temp : items.entrySet()) {
            dumpItem(temp.getValue(),map);
        }

        server.dispose();
        return map;
    }

    public void wirte(double NH3){
        Map<String,Double> map = Maps.newHashMap();

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
            item = group.addItem("output.AMMONIA_pipe_flux");
        } catch (JIException e) {
            e.printStackTrace();
        } catch (AddFailedException e) {
            e.printStackTrace();
        }

        final JIVariant value = new JIVariant(NH3);

        try {
            item.write(value);
        } catch (JIException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        server.dispose();
    }

    private static Map dumpItem(Item item, Map<String,Double> map) {
        ItemState itemState = null;
        try {
            itemState = item.read(false);
            Integer type = itemState.getValue().getType();
            //JIVariant.VT_R4对应float，对应double，两种处理方法一致
            if (type == JIVariant.VT_R8) {
                JIVariant value = itemState.getValue();
                double number = value.getObjectAsDouble();
                map.put(item.getId(),number);
            }
        } catch (JIException e) {
            e.printStackTrace();
        }

        return map;

    }

}
