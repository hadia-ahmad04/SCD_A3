package com.mycompany.librarygui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText("READ");
        return this;
    }
}

public class LibraryGUI {
    private JFrame frame;
    private JTable itemTable;
    private DefaultTableModel tableModel;
    private JButton addItemButton;
    private JButton editItemButton;
    private JButton deleteItemButton;
    private JButton viewPopularityButton;

    public LibraryGUI() {
        frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String[] columnNames = {"Title", "Author", "Publication Year", "Read Item"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) {
                    return JButton.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };

        itemTable = new JTable(tableModel);
        itemTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());

        JScrollPane scrollPane = new JScrollPane(itemTable);

        addItemButton = new JButton("Add Item");
        editItemButton = new JButton("Edit Item");
        deleteItemButton = new JButton("Delete Item");
        viewPopularityButton = new JButton("View Popularity");

        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddItemDialog();
            }
        });

        editItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openEditItemDialog();
            }
        });

        deleteItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDeleteItemDialog();
            }
        });

        viewPopularityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] popularityData = {100, 150, 200, 120};
                displayPopularityChart(popularityData);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addItemButton);
        buttonPanel.add(editItemButton);
        buttonPanel.add(deleteItemButton);
        buttonPanel.add(viewPopularityButton);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setSize(600, 600);
        frame.setVisible(true);

        populateDataFromFile("data.txt");
    }

    private void populateDataFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String title = parts[0].trim();
                    String author = parts[1].trim();
                    String publicationYear = parts[2].trim();

                    JButton readItemButton = new JButton("READ");
                    readItemButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JOptionPane.showMessageDialog(frame, "Reading: " + title);
                        }
                    });

                    tableModel.addRow(new Object[]{title, author, publicationYear, readItemButton});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayPopularityChart(int[] popularityData) {
        JFrame chartFrame = new JFrame("Popularity Chart");
        chartFrame.setSize(600, 400);

        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBarChart(g, popularityData);
            }
        };

        chartFrame.add(chartPanel);
        chartFrame.setVisible(true);
    }

    private void drawBarChart(Graphics g, int[] data) {
    int barWidth = 50;
    int spacing = 10;
    int x = 50;  
    int maxHeight = 300;
    
    g.drawLine(x, 10, x, maxHeight + 10);
    g.drawString("Popularity", x - 20, 10);

    for (int i = 0; i < data.length; i++) {
        int barHeight = data[i];
        int y = maxHeight - barHeight;

        g.setColor(Color.blue);
        g.fillRect(x, y, barWidth, barHeight);

        g.setColor(Color.black);
        g.drawLine(x, maxHeight + 10, x, maxHeight + 20);
        g.drawString("Item " + (i + 1), x - 15, maxHeight + 35);

        g.drawString(String.valueOf(barHeight), x, y - 5);

        x += barWidth + spacing;
    }
}


    private void openAddItemDialog() {
        JDialog dialog = new JDialog(frame, "Add New Item", true);
        dialog.setLayout(new GridLayout(4, 2));

        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField yearField = new JTextField();
        JButton addButton = new JButton("Add");

        dialog.add(new JLabel("Title:"));
        dialog.add(titleField);
        dialog.add(new JLabel("Author:"));
        dialog.add(authorField);
        dialog.add(new JLabel("Publication Year:"));
        dialog.add(yearField);
        dialog.add(new JLabel()); 
        dialog.add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String author = authorField.getText();
                String year = yearField.getText();

                if (!title.isEmpty() && !author.isEmpty() && !year.isEmpty()) {
                    addDataToTable(title, author, year);
                    dialog.dispose();
                }
            }

            private void addDataToTable(String title, String author, String year) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
        });

        dialog.pack();
        dialog.setVisible(true);
    }

    private void openEditItemDialog() {
        int selectedRow = itemTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select an item to edit.");
        } else {
            JDialog dialog = new JDialog(frame, "Edit Item", true);
            dialog.setLayout(new GridLayout(4, 2));

            JTextField titleField = new JTextField((String) tableModel.getValueAt(selectedRow, 0));
            JTextField authorField = new JTextField((String) tableModel.getValueAt(selectedRow, 1));
            JTextField yearField = new JTextField((String) tableModel.getValueAt(selectedRow, 2));
            JButton editButton = new JButton("Save Changes");

            dialog.add(new JLabel("Title:"));
            dialog.add(titleField);
            dialog.add(new JLabel("Author:"));
            dialog.add(authorField);
            dialog.add(new JLabel("Publication Year:"));
            dialog.add(yearField);
            dialog.add(new JLabel());
            dialog.add(editButton);

            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newTitle = titleField.getText();
                    String newAuthor = authorField.getText();
                    String newYear = yearField.getText();

                    if (!newTitle.isEmpty() && !newAuthor.isEmpty() && !newYear.isEmpty()) {
                        editDataInTable(selectedRow, newTitle, newAuthor, newYear);
                        dialog.dispose();
                    }
                }
            });

            dialog.pack();
            dialog.setVisible(true);
        }
    }

    private void openDeleteItemDialog() {
        int selectedRow = itemTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select an item to delete.");
        } else {
            String title = (String) tableModel.getValueAt(selectedRow, 0);
            int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete the item: " + title + "?", "Delete Item", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                deleteDataFromTable(selectedRow);
            }
        }
    }

    private void saveDataToFile(String fileName) {
        try (BufferedOutputStream os = new BufferedOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String title = (String) tableModel.getValueAt(i, 0);
                String author = (String) tableModel.getValueAt(i, 1);
                String publicationYear = (String) tableModel.getValueAt(i, 2);

               
                os.write((title + "," + author + "," + publicationYear + "\n").getBytes());
            }
        } catch (IOException e) {
        }
    }

    private void openReadItemDialog(String title, String content) {
        ReadItemWindow readItemWindow = new ReadItemWindow(title, content);
        readItemWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                    readItemWindow,
                    "Do you want to exit reading the book?",
                    "Exit Confirmation",
                    JOptionPane.YES_NO_OPTION
                );

                if (choice == JOptionPane.YES_OPTION) {
                    readItemWindow.dispose(); 
                }
            }
        });
    }

    private void addDataToTable(String title, String author, String publicationYear, String content) {
        JButton readItemButton = new JButton("READ");
        readItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openReadItemDialog(title, content);
            }
        });

        tableModel.addRow(new Object[]{title, author, publicationYear, readItemButton});
        saveDataToFile("data.txt");
    }

    private void editDataInTable(int selectedRow, String newTitle, String newAuthor, String newPublicationYear) {
        tableModel.setValueAt(newTitle, selectedRow, 0);
        tableModel.setValueAt(newAuthor, selectedRow, 1);
        tableModel.setValueAt(newPublicationYear, selectedRow, 2);
        saveDataToFile("data.txt");
    }

    private void deleteDataFromTable(int selectedRow) {
        if (selectedRow >= 0 && selectedRow < tableModel.getRowCount()) {
            tableModel.removeRow(selectedRow);
            saveDataToFile("data.txt");
        }
    }
    
    

    
    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibraryGUI());
    }

    private static class ReadItemWindow {

        public ReadItemWindow(String title, String content) {
        }

        private void addWindowListener(WindowAdapter windowAdapter) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private void dispose() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
    }
}

