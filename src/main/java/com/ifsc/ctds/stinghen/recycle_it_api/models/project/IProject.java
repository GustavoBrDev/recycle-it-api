package com.ifsc.ctds.stinghen.recycle_it_api.models.project;

import com.ifsc.ctds.stinghen.recycle_it_api.models.user.User;

public interface IProject {

    void start (User user);
    void end (User user);
}
