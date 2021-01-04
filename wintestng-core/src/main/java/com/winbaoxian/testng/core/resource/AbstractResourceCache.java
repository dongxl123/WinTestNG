package com.winbaoxian.testng.core.resource;

import com.winbaoxian.testng.model.core.resource.ResourceSettings;
import com.winbaoxian.testng.utils.Md5Utils;

public abstract class AbstractResourceCache<T> implements IResourceCache<T> {

    protected ResourceSettings settings;

    @Override
    public String getConfigMd5() {
        return Md5Utils.INSTANCE.md5(settings);
    }

}
