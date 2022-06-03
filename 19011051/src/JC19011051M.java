import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.Dimension;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;

public class JC19011051M extends JFrame implements ActionListener, MouseListener{
	JButton btnAdmin, btnUser;
	JPanel mainPanel, adminPanel, userPanel;
	//ȸ���� �߰������ ���� �г�
	JPanel moviePanel;	
	
	// DatabaseConnection
	String driver, URL, user, password, sql;
	Connection con;
	PreparedStatement pstmt;
	Statement stmt;
    ResultSet rs;
	
	
	//�������� ���ƿ��� ��ư
	JButton btnReturnMainFromAdmin, btnReturnMainFromProfessor, btnReturnMainFromStudent;
	//������ ������ adminPanel�� ��ư
	JButton btnResetDB, btnInsert, btnDelete, btnModify, btnViewAll;
	//ȸ�� ������ userPanel�� ��ư
	JButton btnSearchMovie, btnReservation, btnMovieReservation;
	

	JScrollPane scrollPane; // txtResul�� �־��� JScrollPane
	JTextArea txtResult; // ������� ������ JTextArea (Center�� �� ����)
	
	JTable movieTable; // ���� ���������� ��ȭ ����� �����ִ� table
	
	//JOptionpane �ʱ� ȸ��ID �Է�â 
	JOptionPane inputUserIdPane;
	
	// ���� ���� ���� ��¥�� ���Ƿ� 2021�� 5�� 5�Ϸ� ����
	String nowDate = "2021.05.05";
	
	int memberId = 1; //ȸ�� ���̵� ���� ����

	
	public JC19011051M() {
		setTitle("18011575 ������ / 19011051 ���ֿ�");
		initLayout();
		setVisible(true);
		setBounds(200, 50, 700, 500); //x��ǥ ,y��ǥ , ���α���, ���α���
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		driver = "com.mysql.cj.jdbc.Driver";
        URL = "jdbc:mysql://localhost:3306/madang?&serverTimezone=Asia/Seoul";
        user = "madang";
        password = "madang";
        sql = "INSERT INTO ORDERS VALUES (?,?,?,?,?)";
        
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(URL, user, password);
            System.out.print("con is excuted");

        } catch (Exception e) {
            System.out.println("Connection failed!");
        }
        
	}
	
	public void initLayout() {
			
		//JButton
		btnAdmin = new JButton("������");
		btnUser = new JButton("ȸ��");
		
		btnReturnMainFromAdmin = new JButton("��������");
		
		btnResetDB = new JButton("DB �ʱ�ȭ");
		btnInsert = new JButton("�Է�");
		btnDelete = new JButton("����");
		btnModify = new JButton("����");
		btnViewAll = new JButton("��ü ���̺� ��ȸ");
		
		btnSearchMovie = new JButton("��ȭ ��ȸ/����");
		btnReservation = new JButton("���� ���� ��Ȳ");
		btnMovieReservation = new JButton("������ ��ȭ ����");

		//JPanel
		mainPanel = new JPanel();
		adminPanel = new JPanel();
		userPanel = new JPanel();
		moviePanel = new JPanel();
		add(mainPanel);
		
		mainPanel.setLayout(null);
		mainPanel.add(btnAdmin);
		mainPanel.add(btnUser);
		btnAdmin.setBounds(50, 100, 200, 200);
		btnUser.setBounds(300, 100, 200, 200);
		
		//������ ������
		//adminPanel ���̾ƿ�
		adminPanel.setVisible(false);
		adminPanel.add(btnReturnMainFromAdmin);
		adminPanel.add(btnResetDB);
		adminPanel.add(btnInsert);
		adminPanel.add(btnDelete);
		adminPanel.add(btnModify);
		adminPanel.add(btnViewAll);
		
		//ȸ�� ������
		//userPanel ���̾ƿ�
		userPanel.setVisible(false);
		userPanel.add(btnSearchMovie);
		userPanel.add(btnReservation);
		moviePanel.add(btnMovieReservation);
		
		//ScrollPane, TextArea
		txtResult = new JTextArea();
		txtResult.setEditable(false);
		scrollPane = new JScrollPane(txtResult);
		
		//��ư �̺�Ʈ
        btnAdmin.addActionListener(this);
        btnUser.addActionListener(this);
        btnReturnMainFromAdmin.addActionListener(this);
        
        btnResetDB.addActionListener(this);
		btnInsert.addActionListener(this);
		btnDelete.addActionListener(this);
		btnModify.addActionListener(this);
		btnViewAll.addActionListener(this);
		
		btnSearchMovie.addActionListener(this);
		btnReservation.addActionListener(this);		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btnAdmin) { //������ �������� panel ��ȯ
			add("North", adminPanel);
			add("Center", scrollPane);
			mainPanel.setVisible(false);
			adminPanel.setVisible(true);
			//searchLecturePanel.setVisible(false);
			txtResult.setText("�Է� ���Ŀ� ���� �ȳ���");
		}
		
		else if(e.getSource() == btnUser) { // ����� �������� panel ��ȯ
			
			inputUserIdPane = new JOptionPane();
			String id = inputUserIdPane.showInputDialog("ȸ�����̵� �Է��ϼ���");
			
			if(id != null) {
				
				memberId = Integer.parseInt(id);
				// id �߸� �Է½� ���� ǥ��!
					
				// member ���̺��� memberid ȸ�� ã�� ����
				String query = "SELECT * FROM member WHERE memberid = " + memberId;
				// ���� ����~~~~
				
				
				add("North", userPanel);
				add("Center", scrollPane);
				mainPanel.setVisible(false);
				userPanel.setVisible(true);
			}
		}
		
		else if(e.getSource() == btnReturnMainFromAdmin) { //�������� ���ư���
			adminPanel.setVisible(false);
			mainPanel.setVisible(true);
			
			txtResult.setText("");
		}
		
		else if(e.getSource() == btnInsert) { // ������ �г��� insert ��ư�� ���� ���
			String tableName = JOptionPane.showInputDialog("�Է��� ���̺� �̸��� �Է��Ͻÿ�");
			try {
				
				if(tableName.equals("Movie")) {

                    JTextField movie_id = new JTextField(4);
                    JTextField movie_name = new JTextField(4);
                    JTextField screentime = new JTextField(4);
                    JTextField rating = new JTextField(4);
                    JTextField director = new JTextField(4);
                    JTextField actor = new JTextField(4);
                    JTextField genre = new JTextField(4);
                    JTextField introduce = new JTextField(4);
                    JTextField release = new JTextField(4);
                    JPanel myPanel = new JPanel();
                    
                    myPanel.add(new JLabel("MovieID:"));
                    myPanel.add(movie_id);
                    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
                    
                    myPanel.add(new JLabel("MovieName:"));
                    myPanel.add(movie_name);
                    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
                    myPanel.add(new JLabel("ScreenTime:"));
                    myPanel.add(screentime);
                    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
                    myPanel.add(new JLabel("Rating:"));
                    myPanel.add(rating);
                    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
                    myPanel.add(new JLabel("Director:"));
                    myPanel.add(director);
                    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
                    myPanel.add(new JLabel("Actor:"));
                    myPanel.add(actor);
                    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
                    myPanel.add(new JLabel("Genre:"));
                    myPanel.add(genre);
                    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
                    myPanel.add(new JLabel("Introduce:"));
                    myPanel.add(introduce);
                    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
                    myPanel.add(new JLabel("Release:"));
                    myPanel.add(release);

                    int result = JOptionPane.showConfirmDialog(null, myPanel,"Movie ���̺��� �Ӽ������� �Է��ϼ���.", JOptionPane.OK_CANCEL_OPTION);

                    int movie_id_int = Integer.parseInt(movie_id.getText());
                    String movie_name_str = (movie_name.getText());
                    String screentime_str = (screentime.getText());
                    String rating_str = (rating.getText());
                    String director_str = (director.getText());
                    String actor_str = (actor.getText());
                    String genre_str = (genre.getText());
                    String introduce_str = (introduce.getText());
                    String release_str = (release.getText());

                    try {

                        Statement stmt = con.createStatement();

                        String insertSQL = "insert into Student (movie_id, movie_name, screentime, rating, director, actor, genre, introduce, release)"
                                + "values("+movie_id_int+",'"+movie_name_str+"','"+screentime_str+"', '"+rating_str+"' , '"
                        		+rating_str+"', '"+director_str+"', '"+actor_str+"', '"+genre_str+"', '"+introduce_str+"', '"+release_str+"')";
                        stmt.executeUpdate(insertSQL);

                    }catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "SQL Exception\n"+e1.getMessage(), "����", JOptionPane.ERROR_MESSAGE);
                    }
                }	
			}catch(NumberFormatException e3) {
                JOptionPane.showMessageDialog(null, "�߸��� �Է��Դϴ�\n"+e3.getMessage(), "����", JOptionPane.ERROR_MESSAGE);
                return;
            }
		}
		else if(e.getSource() == btnDelete) {
			
		}
		else if(e.getSource() == btnModify) {
			
		}
		else if(e.getSource() == btnViewAll) {
			viewAll();
		}
		else if(e.getSource() == btnResetDB) {
			initDatabase();
		}
		else if (e.getSource() == btnSearchMovie) {
			searchMovie();
		}
		else if (e.getSource() == btnReservation) {
			
		}
		else if (e.getSource() == btnMovieReservation) {
			movieReservation(memberId);
		}
		
	}
	
	private void executeSQL(String[] arr) {
		try {
			stmt = con.createStatement();
			for (int i = 0; i < arr.length; ++i) {
				stmt.executeUpdate(arr[i]);
			}
			
		} catch (SQLException e){
			JOptionPane.showMessageDialog(null, e, "���� �޽���", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private void initDatabase() {
		String[] initSQL = new String[4];
		String[] createSQL = new String[7];
		String[] insertMovieSQL = new String[12];
		initSQL[0] = "DROP DATABASE IF EXISTS madang;";
		initSQL[1] = "create database madang;";
		initSQL[2] = "commit;";
		initSQL[3] = "USE madang";
		
		createSQL[0] = "CREATE TABLE IF NOT EXISTS `madang`.`Movie` (\n"
				+ "  `movie_id` INT NOT NULL,\n"
				+ "  `movie_name` VARCHAR(45) NULL,\n"
				+ "  `screentime` VARCHAR(45) NULL,\n"
				+ "  `rating` VARCHAR(45) NULL,\n"
				+ "  `director` VARCHAR(45) NULL,\n"
				+ "  `actor` VARCHAR(45) NULL,\n"
				+ "  `genre` VARCHAR(45) NULL,\n"
				+ "  `introduce` VARCHAR(150) NULL,\n"
				+ "  `release` VARCHAR(45) NULL,\n"
				+ "  PRIMARY KEY (`movie_id`))\n"
				+ "ENGINE = InnoDB;";
		createSQL[1] = "CREATE TABLE IF NOT EXISTS `madang`.`Theater` (\n"
				+ "  `theater_id` INT NOT NULL,\n"
				+ "  `seat_num` INT NULL,\n"
				+ "  `theater_use` VARCHAR(45) NULL,\n"
				+ "  PRIMARY KEY (`theater_id`))\n"
				+ "ENGINE = InnoDB;";
		createSQL[2] = "CREATE TABLE IF NOT EXISTS `madang`.`Schedule` (\n"
				+ "  `schedule_id` INT NOT NULL,\n"
				+ "  `date` VARCHAR(45) NULL,\n"
				+ "  `day` VARCHAR(45) NULL,\n"
				+ "  `round` VARCHAR(45) NULL,\n"
				+ "  `time` VARCHAR(45) NULL,\n"
				+ "  `movie_id` INT NOT NULL,\n"
				+ "  `theater_id` INT NOT NULL,\n"
				+ "  PRIMARY KEY (`schedule_id`),\n"
				+ "  INDEX `fk_Schedule_Movie1_idx` (`movie_id` ASC) VISIBLE,\n"
				+ "  INDEX `fk_Schedule_Theater1_idx` (`theater_id` ASC) VISIBLE,\n"
				+ "  CONSTRAINT `fk_Schedule_Movie1`\n"
				+ "    FOREIGN KEY (`movie_id`)\n"
				+ "    REFERENCES `madang`.`Movie` (`movie_id`)\n"
				+ "    ON DELETE NO ACTION\n"
				+ "    ON UPDATE NO ACTION,\n"
				+ "  CONSTRAINT `fk_Schedule_Theater1`\n"
				+ "    FOREIGN KEY (`theater_id`)\n"
				+ "    REFERENCES `madang`.`Theater` (`theater_id`)\n"
				+ "    ON DELETE NO ACTION\n"
				+ "    ON UPDATE NO ACTION)\n"
				+ "ENGINE = InnoDB;\n"
				+ "";
		createSQL[3] = "CREATE TABLE IF NOT EXISTS `madang`.`Seat` (\n"
				+ "  `seat_id` INT NOT NULL,\n"
				+ "  `seat_use` VARCHAR(45) NULL,\n"
				+ "  `theater_id` INT NOT NULL,\n"
				+ "  PRIMARY KEY (`seat_id`),\n"
				+ "  INDEX `fk_Seat_Theater1_idx` (`theater_id` ASC) VISIBLE,\n"
				+ "  CONSTRAINT `fk_Seat_Theater1`\n"
				+ "    FOREIGN KEY (`theater_id`)\n"
				+ "    REFERENCES `madang`.`Theater` (`theater_id`)\n"
				+ "    ON DELETE NO ACTION\n"
				+ "    ON UPDATE NO ACTION)\n"
				+ "ENGINE = InnoDB;";
		createSQL[4] = "CREATE TABLE IF NOT EXISTS `madang`.`Member` (\n"
				+ "  `member_id` INT NOT NULL,\n"
				+ "  `member_name` VARCHAR(45) NULL,\n"
				+ "  `phone` VARCHAR(45) NULL,\n"
				+ "  `mail` VARCHAR(45) NULL,\n"
				+ "  PRIMARY KEY (`member_id`))\n"
				+ "ENGINE = InnoDB;";
		createSQL[5] = "CREATE TABLE IF NOT EXISTS `madang`.`Reservation` (\n"
				+ "  `reservation_id` INT NOT NULL,\n"
				+ "  `pay_method` VARCHAR(45) NULL,\n"
				+ "  `pay_status` VARCHAR(45) NULL,\n"
				+ "  `pay_amount` VARCHAR(45) NULL,\n"
				+ "  `pay_date` VARCHAR(45) NULL,\n"
				+ "  `member_id` INT NOT NULL,\n"
				+ "  PRIMARY KEY (`reservation_id`),\n"
				+ "  INDEX `fk_Reservation_Member1_idx` (`member_id` ASC) VISIBLE,\n"
				+ "  CONSTRAINT `fk_Reservation_Member1`\n"
				+ "    FOREIGN KEY (`member_id`)\n"
				+ "    REFERENCES `madang`.`Member` (`member_id`)\n"
				+ "    ON DELETE NO ACTION\n"
				+ "    ON UPDATE NO ACTION)\n"
				+ "ENGINE = InnoDB;";
		createSQL[6] = "CREATE TABLE IF NOT EXISTS `madang`.`Ticket` (\n"
				+ "  `ticket_id` INT NOT NULL,\n"
				+ "  `issue` VARCHAR(45) NULL,\n"
				+ "  `std_price` VARCHAR(45) NULL,\n"
				+ "  `price` VARCHAR(45) NULL,\n"
				+ "  `theater_id` INT NOT NULL,\n"
				+ "  `schedule_id` INT NOT NULL,\n"
				+ "  `seat_id` INT NOT NULL,\n"
				+ "  `reservation_id` INT NOT NULL,\n"
				+ "  PRIMARY KEY (`ticket_id`),\n"
				+ "  INDEX `fk_Ticket_Schedule1_idx` (`schedule_id` ASC) VISIBLE,\n"
				+ "  INDEX `fk_Ticket_Seat1_idx` (`seat_id` ASC) VISIBLE,\n"
				+ "  INDEX `fk_Ticket_Reservation1_idx` (`reservation_id` ASC) VISIBLE,\n"
				+ "  INDEX `fk_Ticket_Theater1_idx` (`theater_id` ASC) VISIBLE,\n"
				+ "  CONSTRAINT `fk_Ticket_Schedule1`\n"
				+ "    FOREIGN KEY (`schedule_id`)\n"
				+ "    REFERENCES `madang`.`Schedule` (`schedule_id`)\n"
				+ "    ON DELETE NO ACTION\n"
				+ "    ON UPDATE NO ACTION,\n"
				+ "  CONSTRAINT `fk_Ticket_Seat1`\n"
				+ "    FOREIGN KEY (`seat_id`)\n"
				+ "    REFERENCES `madang`.`Seat` (`seat_id`)\n"
				+ "    ON DELETE NO ACTION\n"
				+ "    ON UPDATE NO ACTION,\n"
				+ "  CONSTRAINT `fk_Ticket_Reservation1`\n"
				+ "    FOREIGN KEY (`reservation_id`)\n"
				+ "    REFERENCES `madang`.`Reservation` (`reservation_id`)\n"
				+ "    ON DELETE NO ACTION\n"
				+ "    ON UPDATE NO ACTION,\n"
				+ "  CONSTRAINT `fk_Ticket_Theater1`\n"
				+ "    FOREIGN KEY (`theater_id`)\n"
				+ "    REFERENCES `madang`.`Theater` (`theater_id`)\n"
				+ "    ON DELETE NO ACTION\n"
				+ "    ON UPDATE NO ACTION)\n"
				+ "ENGINE = InnoDB;";
		insertMovieSQL[0] = "INSERT INTO Movie VALUES(1, '�Ű��Բ�-�˿� ��', '139��', '12�� �̻� ������', '���ȭ', '������, ������, ������, �����', '��Ÿ��', '��ȣ�� �۰��� ���� �Ű��Բ��� ��ȭȭ ��ǰ. 1���� �˿� �� ���� ������ ���� ���� ������� �����Ǿ���. �� ������ ����ȫ�� 7���� ���ǰ� ���Ͱ� �̽¿��� ���̰� �ִ� �ҵ��� ���� �ٷ�� �ִ�.', '2021.01.07');"; 
		insertMovieSQL[1] = "INSERT INTO Movie VALUES(2, '�������α׳�', '137��', '15�� �̻� ������', '�����', '������, ������', '�ڹ̵�, ���, �θǽ�', '1999�� 8������ ��� ���л��̴� �߿�74(���� ��ȣ��)�� ID�� �� ��Ƽ���� PC��� ���촩�� ���Ӷ����� �����Ͽ� ��û�� ȣ���� �޾Ҵ� ������ �Ҽ��� �������� �� �� ���� ���л��� ����߶��� ���� ���丮.', '2021.02.24');";
		insertMovieSQL[2] = "INSERT INTO Movie VALUES(3, '�̳���', '115��', '12�� �̻� ������', '���̻�', '��Ƽ�� ��, �ѿ���, ������, �ٷ� ��', '���', '1980��� �̱����� �̹ΰ� �ѱ� �̹��� ������ �ð񿡼� ������ ����� �̾߱⸦ �ٷ� �̱� ��ȭ��.', '2021.03.03');";
		insertMovieSQL[3] = "INSERT INTO Movie VALUES(4, '���� ������ ��ȸ', '128��', '12�� �̻� ������', '���� ����', '�κ� ��������', '���', '1959�� ����Ʈ�� ���ű���[9] ���� �б� �����Ⱑ ���� ǳ��� �縳�б����� �������� �Ϸ��� ���� Ȱ���� �� ����� �ϰ� ������, ��� �̷� ������ �� ��ȭ�� ������ �� ������ �������� ������ �������� �ϰ� �ִ�.', '2021.04.01');";
		insertMovieSQL[4] = "INSERT INTO Movie VALUES(5, '�г��� ����:�� ��Ƽ����Ʈ', '142��', '12�� �̻� ������', '����ƾ ��', '�� ����, �� �ó�, �� ��, �̼� �ε帮����', '�׼�', '�г��� ���� �ø����� 9��° ��ǰ���� �ø����� �������� ����� Ʈ�������� ù ��. �����ۿ��� ����� �����ߴ� ���� �������� �����Ͽ��� ���ΰ� ���̴��� ������ ������ �䷹�� ������ �� �ó��� �⿬�Ѵ�.', '2021.05.19');";
		insertMovieSQL[5] = "INSERT INTO Movie VALUES(6, '�߽�����', '94��', '15�� �̻� ������', '��â��', '������, ������, ����, ����ȣ', '���', '�ѱ��� ������ ��ȭ. �� �׷� ���̺�, ǥ��, ������ ����, ŷ������ ����� �ټ��� �ѱ���ȭ�� ������ �þҴ� ��â���� ���� ���� �������̸�, ������ ��ȭ <��Ʈ�����: ��¡�� ��> (2015)�� �ѱ� ������ũ���̴�.', '2021.06.23');";
		insertMovieSQL[6] = "INSERT INTO Movie VALUES(7, '�𰡵�', '121��', '15�� �̻� ������', '���¿�', '������, ���μ�, ����ȣ, ����ȯ', '�׼�', '�������� ���� ���� ����, �𰡵� ���ݺ��� �츮�� ��ǥ�� ������ �����̴�! ���ѹα��� UN������ ���� ���м����ϴ� �ñ� 1991�� �Ҹ������� ���� �𰡵𽴿����� ��������� ������ �Ͼ��.', '2021.07.28');";
		insertMovieSQL[7] = "INSERT INTO Movie VALUES(8, '��ũȦ', '113��', '12�� �̻� ������', '������', '���¿�, �輺��, �̱���, �Ǽ���', '�ڹ̵�, �糭, ���', '��ũȦ ������ �ּ���� �ٷ� ��ȭ�̴�.������ ������ ���� ��ǰ�̸�, ����Ʈ �ܿ��� �������� �� ������ �ʴ� �ڹ̵�� �糭���� ���� ���� ��Ÿ���� ��ǰ�̴�.', '2021.08.11');";
		insertMovieSQL[8] = "INSERT INTO Movie VALUES(9, '����', '117��', '12�� �̻� ������', '������', '������, �̼���, ����, �̼���, �谭��', '���', '���� �� �ִ� ���� ������ۿ� ������ ���� �������� ���� ����. ���úη� û�ʹ뿡 �� 54��° ������ ���� �ذ�(������)�� ��ǥ�� �� �ϳ�! �ٷ� ������ �������� ����� ���̴�.', '2021.09.15');";
		insertMovieSQL[9] = "INSERT INTO Movie VALUES(10, '���� 2:�� ���� �� ī����', '97��', '15�� �̻� ������', '�ص� ��Ű��', '���ϵ�, ��� �ط���', '�׼�, SF', '���� �ǻ翵ȭ �ø����� 2��° ��ǰ����, �Ҵ� �����̴��� ���Ϲ����� 2��° ��ǰ�̴�.', '2021.10.13');";
		insertMovieSQL[10] = "INSERT INTO Movie VALUES(11, '���ֺ��� �θǽ�', '95��', '15�� �̻� ������', '������', '������, �ռ���, ������, �载��, ������', '�θǽ�', '�ϵ� ���ֵ� ������� ���� �ʴ� ������ȩ ���ڿ���(������). �� ��ģ���� ���� �̺� �� ȣ��Ӱ� ���� ���� ���������� ���� �� ���� �ܷο� �� �̰� ������ ������ ������ ���÷� ��븦 �˻��Ѵ�.', '2021.11.24');";
		insertMovieSQL[11] = "INSERT INTO Movie VALUES(12, '�����̴���:�� ���� Ȩ', '148��', '12�� �̻� ������', '�� ����', '�� Ȧ����, �����̾� �ݸ�, ���׵�Ʈ �Ĺ���ġ, ������ �����', '�׼�, ����, SF', '���� �ó׸�ƽ ���Ϲ����� 27��° ���� ��ȭ. ���� ������ 4�� 4��° ��ȭ���� ���� ��Ʃ��� �����̴��� �ø����� 3��° ��ǰ���� ���� ��Ʃ��� �����̴��� �ø����� ù��° Ʈ�������� ȨĿ�� Ʈ�������� ������ ��ǰ�̴�.', '2021.12.15');";
		
		//������ string�� ������
		executeSQL(initSQL);
		executeSQL(createSQL);
		executeSQL(insertMovieSQL);
		//executeSQL(insertSQL, insertSQL.length);
		JOptionPane.showMessageDialog(null, "�ʱ�ȭ �Ϸ�", "�˸�", JOptionPane.DEFAULT_OPTION);
	}
	
	// �Ӽ����� ���� �迭 ��ȯ
	private String[] getAttribute(String table) {
		String columns = "";
		try {
			ResultSet rs = con.getMetaData().getColumns(null, "madang", table, "%");
			while(rs.next()) {
			    String name = rs.getString("COLUMN_NAME");
			    columns += name + " ";
			}
		
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] result = columns.split(" ");
		return result;
	}
	
	private JTable getTable(String table, String condition) {
		String query = "";
		if (condition != "")
			query = condition;
		else
			query = "SELECT * FROM " + table + ";";
		String countQuery = "SELECT COUNT(*) from " + table + ";";
		int i = 0;
		
		//�Ӽ��� �޾ƿ���
		String columnName[] = getAttribute(table);
		
		//������ ������ �Ӽ� ����(attribute)�� header�� ���� ���̺��� �����
		try {
			//stmt �� data ����
			Statement stmt1 = con.createStatement();
			Statement stmt2 = con.createStatement();
			ResultSet rs = stmt2.executeQuery(query);
			
			//Ʃ���� ���� ��������
			ResultSet countRS = stmt1.executeQuery(countQuery);
			countRS.next();
			int count = countRS.getInt(1);
			
			//�����ͺ��̽� ũ�⸸ŭ�� 2���� �迭 ����
			String data[][] = new String[count][columnName.length];
			
			//2���� �迭 data�� table ��ȸ�ϸ� ������ ����
			i = 0;
			while(rs.next()) {
				for (int j = 0; j < columnName.length; ++j)
					data[i][j] = rs.getString(columnName[j]);
				++i;
	  	  	 }
			
			//�տ��� ���� 2���� �迭�� headerLine�� �̿��� JTable��ü ����
			DefaultTableModel model = new DefaultTableModel(data, columnName);
			JTable viewTable = new JTable(model);
			viewTable.setShowGrid(true);
			viewTable.setShowVerticalLines(true);
			
			return viewTable;
			
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, "SQL ���� ����", "���� �޽���", JOptionPane.WARNING_MESSAGE);
		}
		return null;		
	}
	
	private void viewAll() {	
		JFrame tableJf = new JFrame("��ü ���̺� ���");
		JPanel stock = new JPanel();
		JPanel mini;
		JLabel label;
		JScrollPane tableData;
		String tableName[] = {"Movie", "Theater", "Schedule", "Seat", "Member", "Reservation", "Ticket"};
		
		for (int i = 0; i < 7; ++i) {
			//getTable �޼ҵ带 �̿��� ���̺��� �޾ƿ���
			tableData = new JScrollPane(getTable(tableName[i], ""), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			tableData.setPreferredSize(new Dimension(500, 200));
			
			//label ���̰� jFrame�� �߰��ϱ�
			mini = new JPanel();
			mini.setLayout(new BoxLayout(mini, BoxLayout.Y_AXIS));
			label = new JLabel(tableName[i]);
			mini.add(label);
			mini.add(tableData);
			stock.add(mini);
		}
		tableJf.add(stock);
		tableJf.setSize(1050, 950);
		tableJf.setVisible(true);
		
	}
	
	// �Ʒ��� ȸ�� ��� ����
	
	// ��� ��ȭ�� ���� ��ȸ ���
	public void searchMovie() {
		String sql = "SELECT * FROM Movie WHERE";
		String[] expression = {" movie_name == ", " director == ", " actor == ", " genre == "};  
		String inputText = JOptionPane.showInputDialog("�˻� ������ ��ȭ��, ������, ����, �帣 ������ �Է��� �ּ���"
				+ "\nex)'����ȣ'�� ��ȭ ��ȸ"
				+ "\n���� '����ȣ' ���� ����");
		String[] condition = inputText.split(" ");
		if (condition.length != 4) {
			JOptionPane.showMessageDialog(null, "��Ŀ� ���� �Է��� �ּ���!", "���� �޽���", JOptionPane.WARNING_MESSAGE);
			return;
		}
		for (int i = 0; i < 4; ++i) {
			if (condition[i].equals("����")) continue;
			sql += expression[i] + condition[i] + " and";
		}
		if (sql.charAt(sql.length() - 1) == 'd') {
			sql = sql.substring(0, sql.length() - 4);
		}
		else {
			sql = "SELECT * FROM Movie";
		}
		sql += ";";
		
		//�˻� ���ǿ� �����ϴ� ���̺��� ���� â ����
		JFrame tableJf = new JFrame("��ȭ ��ȸ");
		tableJf.setLocation(400, 300);
		tableJf.setSize(700, 450);
		
		//��ȭ ����Ʈ�� ���̺�� �ҷ�����
		movieTable = getTable("Movie", sql);
		movieTable.addMouseListener(this);
		
		JScrollPane tableData = new JScrollPane(movieTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tableData.setPreferredSize(new Dimension(500, 200));

		JPanel mini = new JPanel();
		mini.setLayout(new BoxLayout(mini, BoxLayout.Y_AXIS));
		JLabel label = new JLabel("�˻��� ���ǿ� �����ϴ� ��ȭ ��ȸ");
		mini.add(label);
		mini.add(tableData);
		mini.add(btnMovieReservation);
		
		tableJf.add(mini);
		tableJf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tableJf.setVisible(true);
	}
	
	// searchMovie���� ��ȸ�� ��ȭ�� ���� ���� ���
	public void movieReservation(int movie_id) {
		System.out.println("����~~");
	}
	
	public void mouseClicked(MouseEvent e) {
		JTable table = (JTable)e.getComponent();
		TableModel model = (TableModel)table.getModel();
		int column = table.getSelectedColumn();
		int row = table.getSelectedRow();
		System.out.println("����");
		
	}
	
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	
	public static void main(String[] args) { 
		JC19011051M jc19011051 = new JC19011051M();
	}


	
}
	


