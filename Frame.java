import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Frame {
    public JFrame frame;
    public Main logObj = new Main();
    public JFrame searchFrame;
    public JLabel txt;
    public JTextField txtField;
    public JTextField FIO;
    public JTextField adress;
    public JTextField number;
    public JFrame createFrame;
    public JPanel mainPanel;
    public JPanel buttonPanel;
    public JTextArea infoArea;
    public JScrollPane scrollPane;
    public JButton create;
    public JButton search;
    public JComboBox<String> selectFilter;
    public boolean num = true;//для отслеживания режима фильтрации
    //главное окно
    public void drawFrame(){
        //читаем данные из файла
        readContacts();
        //создаем выпадающий список для выбора фильтрации и слушателя для него
        selectFilter = new JComboBox<>();
        //selectFilter.setEditable(true);
        selectFilter.addItem("Фильтрация по Ф.И.О");
        selectFilter.addItem("Фильтрация по Адресу");
        selectFilter.addActionListener(new selectListener());

        //создаем кнопку для вызова окна поиска и добавляем слушателя
        search = new JButton("Поиск");
        search.addActionListener(new searchListener());

        // создаем кнопку для создания контактка и добавляем слушателя
        create = new JButton("Создать");
        create.addActionListener(new createListener());

        //создаем хрень которая забыл как называется но отображает текст и c инглиша area
        infoArea = new JTextArea();
        writeInArea();

        //и делаем для нее полосу прокрутки
        scrollPane = new JScrollPane(infoArea);

        //cоздаем двe панели на которых будет обвешан весь этот бардак из элементов, и добавляем элементы
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));
        buttonPanel.add(create);
        buttonPanel.add(search);
        buttonPanel.add(selectFilter);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.EAST);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        //создаем фрейм и добавляем все в него
        frame = new JFrame("Telephone directory");
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(700,200);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

    }
    //слушатель для кнопки поиска
    public class searchListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            drawSearchFrame() ;
        }
    }

    //окно для поиска
    public void drawSearchFrame(){
        txtField = new JTextField();
        txt = new JLabel();
        txt.setText("Ф.И.О/Адресс/Номер");
        JButton find = new JButton("Найти");
        find.addActionListener(new findListener());
        JButton close = new JButton("Отмена");
        close.addActionListener(new closeListener());
        JButton edit = new JButton("Ред.");
        edit.addActionListener(new editListener());
        JButton delete = new JButton("Удалить");
        delete.addActionListener(new deleteListener());
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(txtField);
        panel.add(find);
        panel.add(txt);
        panel.add(edit);
        panel.add(delete);
        panel.add(close);
        searchFrame = new JFrame("Поиск");
        searchFrame.getContentPane().add(BorderLayout.CENTER,panel);
        searchFrame.setSize(300,170);
        searchFrame.setResizable(false);
        searchFrame.setVisible(true);
        searchFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        searchFrame.setLocationRelativeTo(null);
    }
    //окно для создания контакта
    public void drawCreateFrame(String line){
        String[] str = line.split("/");
        FIO = new JTextField(str[0]);
        adress = new JTextField(str[1]);
        number = new JTextField(str[2]);
        JButton save = new JButton("Сохранить");
        save.addActionListener(new saveListener());
        JButton close = new JButton("Отмена");
        close.addActionListener(new closeListener());
        JPanel createPanel = new JPanel();
        createPanel.setLayout(new BoxLayout(createPanel,BoxLayout.Y_AXIS));
        createPanel.add(FIO);
        createPanel.add(adress);
        createPanel.add(number);
        createPanel.add(save);
        createPanel.add(close);
        createFrame = new JFrame();
        createFrame.getContentPane().add(BorderLayout.CENTER,createPanel);
        createFrame.setSize(300,150);
        createFrame.setResizable(false);
        createFrame.setVisible(true);
        createFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        createFrame.setLocationRelativeTo(null);
    }
    //слушатель кнопки редактировать
    public class editListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            drawCreateFrame(txt.getText());
            logObj.contacts.remove(txt.getText());
            writeContacts();
            searchFrame.dispose();
        }
    }
    //слушатель для кнопок отмена
    public class closeListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            searchFrame.dispose();
            createFrame.dispose();
        }
    }
    //слушатель для кнопки найти
    public class findListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            for(int i = 0; i!= logObj.contacts.size();i++) {
                String[] element = logObj.contacts.get(i).split("/");
                System.out.println(logObj.contacts.get(i));
                for (int k = 0; k!=3;k++){
                    System.out.println(element[k]);
                    if(element[k].equals(txtField.getText())){
                        txt.setText(logObj.contacts.get(i));
                        System.out.println(123);
                        break;
                    }
                }
            }
        }
    }
    //слушатель для кнопки создать
    public class createListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            drawCreateFrame("Ф.И.О/Адресс/Номер");
        }
    }
    //слушатель для выплывающего окна
    public class selectListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
                writeInArea();
        }
    }
    //слушатель для кнопки сохранить
    public class saveListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String str;
            //берем текст из полей
            String strFIO = FIO.getText();
            String strAdress = adress.getText();
            String strNumber = number.getText();
            if(num) {
                str = strAdress + "/" + strFIO + "/" + strNumber;}
            else{
                str = strFIO + "/" + strAdress + "/" + strNumber;
            }
            //добавляем его в коллекцию
            logObj.contacts.add(str);
            //сохраняем коллекцию в документ
            writeContacts();
            createFrame.dispose();
            writeInAreaWhithoutChange();
        }
    }
    //слушатель для кнопки удалить
    public class deleteListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            logObj.contacts.remove(txt.getText());
            writeContacts();
            searchFrame.dispose();
            writeInAreaWhithoutChange();
        }
    }
    //метод записи в текстовое без изменений фильтра
    public void writeInAreaWhithoutChange(){
        infoArea.setText("");
        ArrayList<String> lines = logObj.contacts;
        Collections.sort(lines);
        for(int i =0;i!=lines.size();i++){
            infoArea.append(lines.get(i) + "\n");
        }

    }
    //метод для записи массива данных в txt файл
    public void writeContacts(){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("contacts.txt"));
            for (int i = 0;i!= logObj.contacts.size() ;i++){
                writer.write(logObj.contacts.get(i) + "\n");
                writer.flush();
            }

        }catch (IOException ex){ex.printStackTrace();}
    }
    //чтение данных из файла в массив
    public void readContacts(){
        try{
            logObj.contacts.clear();
            System.out.println(logObj.contacts.size());
            BufferedReader reader = new BufferedReader(new FileReader("contacts.txt"));
            String  line = null;
            while((line = reader.readLine())!=null){
                logObj.contacts.add(line);
                System.out.println("reading");
                System.out.println(logObj.contacts.get(logObj.contacts.size() -1 ));
            }
            System.out.println(logObj.contacts.size());
        }catch(IOException ex){ex.printStackTrace();}
    }
    //записиь данных в текстовое поле с изменением фильтра
    public void writeInArea(){
        infoArea.setText("");
        ArrayList<String> lines;
        if(num = true){
            lines = getChangedList(logObj.contacts);
            num = false;
        }
        else{
            lines = logObj.contacts;
            num = true;
        }
        Collections.sort(lines);
        for (int i = 0; i!=lines.size();i++){
            infoArea.append(lines.get(i) + "\n");
        }
    }
    //метод для замены строк фио на адресс
    public ArrayList getChangedList(ArrayList<String> list){
        ArrayList<String> DList = new ArrayList<String>();
        for(int k = 0;k!= list.size();k++){
            System.out.println(k);
            String[] element = list.get(k).split("/");
            String str = element[1] + "/" + element[0] +"/"+ element[2];
            DList.add(str);
        }
        list.clear();
        list.addAll(DList);
        return list;
    }

}
