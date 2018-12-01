import com.oracle.xmlns.internal.webservices.jaxws_databinding.ExistingAnnotationsType;

import javax.swing.*;

public class HomeFrame extends JFrame {
    public HomeFrame(){

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(600,600);
        this.setResizable(false);
        this.setVisible(true);
    }

    public static void main(String[] args){
        HomeFrame frame = new HomeFrame();
    }
}
