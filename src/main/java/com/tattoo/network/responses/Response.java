package com.tattoo.network.responses;

import java.io.Serializable;

public interface Response extends Serializable {
    boolean isSuccess();
    String getMessage();
}