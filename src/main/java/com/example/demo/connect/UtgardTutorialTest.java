package com.example.demo.connect;

import com.example.demo.enums.CEMS;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.dcom.list.ClassDetails;
import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.*;
import org.openscada.opc.lib.da.browser.Branch;
import org.openscada.opc.lib.da.browser.FlatBrowser;
import org.openscada.opc.lib.da.browser.Leaf;
import org.openscada.opc.lib.list.Categories;
import org.openscada.opc.lib.list.Category;
import org.openscada.opc.lib.list.ServerList;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Executors;

public class UtgardTutorialTest {

    public static void main(String[] args) throws JIException, UnknownHostException, NotConnectedException, DuplicateGroupException, AddFailedException, InterruptedException, AlreadyConnectedException {

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
        }

        Group group = server.addGroup("input");


        Map<String, Item> items = group.addItems(CEMS.CEMS_in_NOX.getStr(),CEMS.CEMS_in_SO2.getStr(),
                CEMS.CEMS_in_flux.getStr(),CEMS.CEMS_in_O2.getStr(),CEMS.CEMS_in_temp.getStr());

       // dumpItem(item);

 for (Map.Entry<String, Item> temp : items.entrySet()) {
            dumpItem(temp.getValue());
        }

        server.dispose();
    }

    private static void dumpFlat(final FlatBrowser browser)
            throws IllegalArgumentException, UnknownHostException, JIException {
        for (String name : browser.browse()) {
            System.out.println(name);
        }
    }

    private static void dumpTree(final Branch branch, final int level) {

        for (final Leaf leaf : branch.getLeaves()) {
            dumpLeaf(leaf, level);
        }
        for (final Branch subBranch : branch.getBranches()) {
            dumpBranch(subBranch, level);
            dumpTree(subBranch, level + 1);
        }
    }

    private static String printTab(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("\t");
        }
        return sb.toString();
    }

    private static void dumpLeaf(final Leaf leaf, final int level) {
        System.out.println(printTab(level) + "Leaf: " + leaf.getName() + ":"
                + leaf.getItemId());
    }

    private static void dumpBranch(final Branch branch, final int level) {
        System.out.println(printTab(level) + "Branch: " + branch.getName());
    }


    private static void dumpItem(Item item) throws JIException {
        ItemState itemState = item.read(false);
        Integer type = itemState.getValue().getType();
        //JIVariant.VT_R4对应float，对应double，两种处理方法一致
        if (type == JIVariant.VT_R8) {
            JIVariant value = itemState.getValue();
            double number = value.getObjectAsDouble();
            System.out.println(number);

        }

    }


    private static int count;

    private static final int PERIOD = 100;

    private static final int SLEEP = 2000;

}
