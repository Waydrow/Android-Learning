package com.waydrow.weibo;

/**
 * Created by slomo on 2016/3/14.
 */
public class FragmentController {
    private static FragmentController controller;

    public static FragmentController getInstance(){
        if(controller == null) {
            controller = new FragmentController();
        }
        return controller;
    }

    private FragmentController(){}
}
