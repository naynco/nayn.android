package co.nayn.core.data.model;

import java.io.Serializable;

public class ResponseModel<T> extends Status implements Serializable{

    T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
