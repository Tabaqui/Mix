/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alexandr
 */
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

public class BikeEngine implements ActionListener  {
   
    public void actionPerformed(ActionEvent e) {
               
        JOptionPane.showConfirmDialog(null, "Пожалуйста поттвердите заказ",
                "Поттвердение заказа", JOptionPane.PLAIN_MESSAGE);
        
    
}
    
}
