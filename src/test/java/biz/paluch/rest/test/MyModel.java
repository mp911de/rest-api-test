package biz.paluch.rest.test;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MyModel {

    private int result;

    public MyModel() {
        super();
    }

    public MyModel(int result) {
        super();
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
