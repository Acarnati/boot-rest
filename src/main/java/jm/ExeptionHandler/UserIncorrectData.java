package jm.ExeptionHandler;

import lombok.Data;

@Data
public class UserIncorrectData {
    private String info;

    public UserIncorrectData() {
    }

    public UserIncorrectData(String info) {
        this.info = info;
    }
}
