package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import static java.awt.Color.*;

public class Main {
    private JFrame mainFrame;
    private JPanel controlPanel;
    private JLabel headerLabel;
    private JPanel bottomPanel;
    private final ObjectMapper mapper;
    private final File file = new File("D:\\proiecte\\java\\ProjectJava\\src\\main\\resources\\data.json");
    public Main(){
        mapper = new ObjectMapper();
        try{
            file.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        prepareGUI();
    }

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.showDemo();
    }
    private void prepareGUI(){
        mainFrame = new JFrame("People Data Form");
        mainFrame.setSize(500,600);
        mainFrame.setLayout(new GridLayout(3,1));
        mainFrame.getContentPane().setBackground(new Color(255, 152, 80));

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });

        headerLabel = new JLabel("", JLabel.CENTER);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(30,0,0,0));
        headerLabel.setFont(new Font("Arial",Font.BOLD,30));
        headerLabel.setForeground(black);

        controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(6,1));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(0,20,0,20));
        controlPanel.setBackground(new Color(189, 224, 254));

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20,20,0,20));
        bottomPanel.setBackground(new Color(189, 224, 254));

        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(bottomPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    private void showDemo() throws IOException {
        headerLabel.setText("Form");

        JPanel marcaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        marcaPanel.add(Box.createVerticalStrut(20));
        JLabel marcaLabel = new JLabel("Marca");
        JTextField marcaText = new JTextField(8);
        createTextLabel(marcaPanel, marcaLabel, marcaText);

        JPanel modelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        marcaPanel.add(Box.createVerticalStrut(20));
        JLabel modelLabel = new JLabel("Model");
        JTextField modelText = new JTextField(8);
        createTextLabel(modelPanel,modelLabel,modelText);

        JPanel combustibilPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        marcaPanel.add(Box.createVerticalStrut(20));
        JLabel combustibilLabel = new JLabel("Combustibil");
        DefaultComboBoxModel<String> combustibilModel = new DefaultComboBoxModel<>();
        combustibilModel.addElement("Diesel");
        combustibilModel.addElement("Benzina");
        combustibilModel.addElement("Hybrid");
        JComboBox<String> combustibilComboBox = new JComboBox<>(combustibilModel);
        JScrollPane combustibilScrollPane = new JScrollPane(combustibilComboBox);
        combustibilPanel.add(combustibilLabel);
        combustibilPanel.add(combustibilScrollPane);

        JPanel culoarePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        marcaPanel.add(Box.createVerticalStrut(20));
        JLabel culoareLabel = new JLabel("Culoare");
        DefaultComboBoxModel<String> culoareModel = new DefaultComboBoxModel<>();
        culoareModel.addElement("Alb");
        culoareModel.addElement("Negru");
        culoareModel.addElement("Albastru");
        culoareModel.addElement("Rosu");
        culoareModel.addElement("Gri");
        culoareModel.addElement("Mov");
        JComboBox<String> culoareComboBox = new JComboBox<>(culoareModel);
        JScrollPane culoareScrollPane = new JScrollPane(culoareComboBox);
        culoarePanel.add(culoareLabel);
        culoarePanel.add(culoareScrollPane);

        JPanel anPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        marcaPanel.add(Box.createVerticalStrut(20));
        JLabel anLabel = new JLabel("An");
        JTextField anText = new JTextField(8);
        createTextLabel(anPanel,anLabel,anText);

        JButton saveButton = new JButton("Salveza Masina");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        marcaPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String marca = marcaText.getText();
                String model = modelText.getText();
                String combustibil = combustibilComboBox.getSelectedItem().toString();
                String culoare = culoareComboBox.getSelectedItem().toString();
                String an = anText.getText();

                Car newCar = new Car();
                newCar.setMarca(marca);
                newCar.setModel(model);
                newCar.setCombustibil(combustibil);
                newCar.setAn(an);
                newCar.setCuloare(culoare);

                try{
                    List<Car> existingCars = mapper.readValue(file, new TypeReference<List<Car>>() {});
                    existingCars.add(newCar);
                    mapper.writeValue(file,existingCars);
                }catch (IOException err){
                    err.printStackTrace();

                }
                marcaText.setText("");
                modelText.setText("");
                anText.setText("");
                try {
                    refreshTable();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(mainFrame,"Masina a fost salvata!");
            }
        });

        controlPanel.add(marcaPanel);
        controlPanel.add(modelPanel);
        controlPanel.add(combustibilPanel);
        controlPanel.add(culoarePanel);
        controlPanel.add(anPanel);
        controlPanel.add(buttonPanel);

        refreshTable();
        mainFrame.setVisible(true);
    }
    private void refreshTable() throws IOException {
        JPanel bottomTextPanel = new JPanel();
        bottomPanel.removeAll();
        String[] columnNames = {"Marca", "Model", "Combustibil", "Culoare", "An","Modifica"};
        List<Car> data = mapper.readValue(file, new TypeReference<List<Car>>() {});
        Object[][] dataNow = convertListToArray(data);
        DefaultTableModel tableModel = new DefaultTableModel(dataNow, columnNames);
        JTable dataTable = new JTable(tableModel);
        dataTable.getColumn("Modifica").setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JCheckBox checkBox = new JCheckBox();
                dataTable.getColumn("Modifica").setCellEditor(new DefaultCellEditor(checkBox));
                checkBox.setSelected((value != null) && ((Boolean) value));
                checkBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(checkBox.isSelected()){
                            int row = dataTable.getSelectedRow();
                            int choice = JOptionPane.showOptionDialog(mainFrame,"Alege o optiune pentru a modifica",
                                    "Modifica data",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE,
                                    null,new Object[]{"Modifica","Sterge","Anuleaza"},"Modifica");
                            if(choice == JOptionPane.YES_OPTION) {
                                String marca = (String)dataTable.getValueAt(row,0);
                                String model = (String)dataTable.getValueAt(row,1);
                                String combustibil = (String)dataTable.getValueAt(row,2);
                                String culoare = (String)dataTable.getValueAt(row,3);
                                String an = (String)dataTable.getValueAt(row,4);
                                data.get(row).setMarca(marca);
                                data.get(row).setModel(model);
                                data.get(row).setCombustibil(combustibil);
                                data.get(row).setCuloare(culoare);
                                data.get(row).setAn(an);

                                try{
                                    mapper.writeValue(file,data);
                                }catch (IOException err){
                                    err.printStackTrace();
                                }
                            }
                            if(choice == JOptionPane.NO_OPTION){
                                int option = JOptionPane.showConfirmDialog(mainFrame,"Esti sigur ca vrei sa stergi?","Stergere in proces",JOptionPane.YES_NO_OPTION);
                                if(option == JOptionPane.YES_OPTION){
                                    data.remove(row);
                                    try{
                                        mapper.writeValue(file,data);
                                        refreshTable();
                                    }catch (IOException err){
                                        err.printStackTrace();
                                    }
                                }
                            }
                            if(choice == JOptionPane.CANCEL_OPTION) {
                                try {
                                    refreshTable();
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }

                            }
                            checkBox.setSelected(false);
                        }

                    }
                });
                return checkBox;
            };
        });

        bottomTextPanel.add(new JScrollPane(dataTable));
        bottomPanel.add(bottomTextPanel);
        bottomPanel.revalidate();
        bottomPanel.repaint();
    }
    private void createTextLabel(JPanel panel, JLabel label, JTextField textField){
        panel.add(label);
        panel.add(textField);
    }

    private Object[][] convertListToArray(List<Car> data) {
        Object[][] dataArray = new Object[data.size()][5];

        for (int i = 0; i < data.size(); i++) {
            Car car = data.get(i);
            dataArray[i][0] = car.getMarca();
            dataArray[i][1] = car.getModel();
            dataArray[i][2] = car.getCombustibil();
            dataArray[i][3] = car.getCuloare();
            dataArray[i][4] = car.getAn();
        }

        return dataArray;
    }

}