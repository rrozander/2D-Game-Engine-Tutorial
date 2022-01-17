package components;

import Jade.Component;

public class SpriteRenderer extends Component {

    private boolean isFirst = false;

    @Override
    public void start() {
        System.out.println("I am Starting");
    }

    @Override
    public void update(float dt) {
        if (!isFirst) {
            System.out.println("I am updating");
            isFirst = true;
        }
    }
}
