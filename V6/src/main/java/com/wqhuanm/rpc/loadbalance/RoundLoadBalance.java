package com.wqhuanm.rpc.loadbalance;

import java.util.List;

public class RoundLoadBalance implements LoadBalance {

    private static int choose = -1;

    @Override
    public String balance(List<String> addressList) {
        choose = (choose + 1) % addressList.size();
        return addressList.get(choose);
    }
}
