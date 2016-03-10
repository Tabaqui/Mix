/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alexandr
 */
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Bike implements ActionListener {

    JButton acc = new JButton("accept");
    JLabel label1 = new JLabel("Bike:");
    JLabel label2 = new JLabel("quantity:");
    JTextField field1 = new JTextField(10);
    JTextField field2 = new JTextField(10);
    JPanel buyBike = new JPanel();
    JPanel inputPanel = new JPanel();
    GridLayout gl = new GridLayout(2, 2);
    BorderLayout bl = new BorderLayout();

    Bike() {
        inputPanel.setLayout(gl);
        inputPanel.add(label1);
        inputPanel.add(field1);
        inputPanel.add(label2);
        inputPanel.add(field2);

        buyBike.setLayout(bl);
        buyBike.add("South", acc);
        buyBike.add("North", inputPanel);

        JFrame frame = new JFrame("BuyBike");
        frame.setContentPane(buyBike);
        frame.pack();
        frame.setVisible(true);

        BikeEngine bikeEngine = new BikeEngine();
//        acc.addActionListener(bikeEngine);
        acc.addActionListener(this);
        
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            check(field1.getText());
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }
    }

    private void check(String field) throws Exception {
        if (field == null) {
            throw new NullPointerException();
        }

        if ("1".equals(field)) {
            throw new CustomException();
        }

        System.out.println("Everything ok =)");
    }

    public static void main(String[] args) {
        Bike bk = new Bike();
    }

}
