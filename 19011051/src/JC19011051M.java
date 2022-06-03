import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.Dimension;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;

public class JC19011051M extends JFrame implements ActionListener{
	JButton btnAdmin, btnUser;
	JPanel mainPanel, adminPanel, userPanel;
	
	
	// DatabaseConnection
	String driver, URL, user, password, sql;
	Connection con;
	PreparedStatement pstmt;
	Statement stmt;
    ResultSet rs;
	
	
	//메인으로 돌아오는 버튼
	JButton btnReturnMainFromAdmin, btnReturnMainFromProfessor, btnReturnMainFromStudent;
	//관리자 페이지 adminPanel의 버튼
	JButton btnResetDB, btnInsert, btnDelete, btnModify, btnViewAll;
	//교수 페이지 professorPanel의 버튼
	JButton btnProfessorLectureInfo, btnStudentInfo, btnDepartmentInfo, btnProfessorTimetable, btnInsertGrade;//btnStudentInfo는 지도 학생 정보 조회하는 버튼

	JScrollPane scrollPane; // txtResul를 넣어줄 JScrollPane
	JTextArea txtResult; // 결과값들 저장할 JTextArea (Center에 들어갈 예정)
	
	//JOptionpane 초기 회원ID 입력창 
	JOptionPane inputUserIdPane;
	int memberId = 1; //회원 아이디 저장 변수

	
	public JC19011051M() {
		setTitle("18011575 정상헌 / 19011051 김주연");
		initLayout();
		setVisible(true);
		setBounds(200, 50, 700, 500); //x좌표 ,y좌표 , 가로길이, 세로길이
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
		btnAdmin = new JButton("관리자");
		btnUser = new JButton("회원");
		
		btnReturnMainFromAdmin = new JButton("메인으로");
		
		btnResetDB = new JButton("DB 초기화");
		btnInsert = new JButton("입력");
		btnDelete = new JButton("삭제");
		btnModify = new JButton("변경");
		btnViewAll = new JButton("전체 테이블 조회");

		//JPanel
		mainPanel = new JPanel();
		adminPanel = new JPanel();
		userPanel = new JPanel();
		add(mainPanel);
		
		mainPanel.setLayout(null);
		mainPanel.add(btnAdmin);
		mainPanel.add(btnUser);
		btnAdmin.setBounds(50, 100, 200, 200);
		btnUser.setBounds(300, 100, 200, 200);
		
		//관리자 페이지
		//adminPanel 레이아웃
		adminPanel.setVisible(false);
		adminPanel.add(btnReturnMainFromAdmin);
		adminPanel.add(btnResetDB);
		adminPanel.add(btnInsert);
		adminPanel.add(btnDelete);
		adminPanel.add(btnModify);
		adminPanel.add(btnViewAll);
		
		//ScrollPane, TextArea
		txtResult = new JTextArea();
		txtResult.setEditable(false);
		scrollPane = new JScrollPane(txtResult);
		
		//버튼 이벤트
        btnAdmin.addActionListener(this);
        btnUser.addActionListener(this);
        btnReturnMainFromAdmin.addActionListener(this);
        
        btnResetDB.addActionListener(this);
		btnInsert.addActionListener(this);
		btnDelete.addActionListener(this);
		btnModify.addActionListener(this);
		btnViewAll.addActionListener(this);
		
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btnAdmin) { //관리자 페이지로 panel 전환
			add("North", adminPanel);
			add("Center", scrollPane);
			mainPanel.setVisible(false);
			adminPanel.setVisible(true);
			//searchLecturePanel.setVisible(false);
			txtResult.setText("입력 형식에 대한 안내…");
		}
		
		else if(e.getSource() == btnUser) { // 사용자 페이지로 panel 전환
			
			inputUserIdPane = new JOptionPane();
			String id = inputUserIdPane.showInputDialog("회원아이디를 입력하세요");
			
			if(id != null) {
				
				memberId = Integer.parseInt(id);
				// id 잘못 입력시 에러 표시!
					
				// member 테이블에서 memberid 회원 찾는 쿼리
				String query = "SELECT * FROM member WHERE memberid = " + memberId;
				// 쿼리 실행~~~~
				
				
				add("North", userPanel);
				add("Center", scrollPane);
				mainPanel.setVisible(false);
				userPanel.setVisible(true);
			}
		}
		
		else if(e.getSource() == btnReturnMainFromAdmin) { //메인으로 돌아가기
			adminPanel.setVisible(false);
			mainPanel.setVisible(true);
			
			txtResult.setText("");
		}
		
		else if(e.getSource() == btnInsert) {
			
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
		
	}
	
	private void executeSQL(String[] arr, int n) {
		try {
			stmt = con.createStatement();
			for (int i = 0; i < n; ++i) {
				System.out.println(i);
				stmt.executeUpdate(arr[i]);
			}
			
		} catch (SQLException e){
			JOptionPane.showMessageDialog(null, e, "오류 메시지", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private void initDatabase() {
		String[] initSQL = new String[4];
		String[] createSQL = new String[7];
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
				+ "  `introduce` VARCHAR(45) NULL,\n"
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
		
		//설정한 string을 실행함
		executeSQL(initSQL, initSQL.length);
		executeSQL(createSQL, createSQL.length);
		//executeSQL(insertSQL, insertSQL.length);
		JOptionPane.showMessageDialog(null, "초기화 완료", "알림", JOptionPane.DEFAULT_OPTION);
	}
	
	// 속성명을 담은 배열 반환
	private String[] getAttribute(String table) {
		String columns = "";
		try {
			ResultSet rs = con.getMetaData().getColumns(null, "madang", table, "%");
			while(rs.next()) {
			    String name = rs.getString("COLUMN_NAME");
			    columns += name + " ";
			    System.out.println(name);
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
		
		//속성명 받아오기
		String columnName[] = getAttribute(table);
		
		//위에서 결정한 속성 개수(attribute)와 header에 따라 테이블을 출력함
		try {
			//stmt 및 data 설정
			Statement stmt1 = con.createStatement();
			Statement stmt2 = con.createStatement();
			ResultSet rs = stmt2.executeQuery(query);
			
			//튜플의 개수 가져오기
			ResultSet countRS = stmt1.executeQuery(countQuery);
			countRS.next();
			int count = countRS.getInt(1);
			
			//데이터베이스 크기만큼의 2차원 배열 선언
			String data[][] = new String[count][columnName.length];
			
			//2차원 배열 data에 table 순회하며 데이터 저장
			i = 0;
			while(rs.next()) {
				for (int j = 0; j < columnName.length; ++j)
					data[i][j] = rs.getString(columnName[j]);
				++i;
	  	  	 }
			
			//앞에서 만든 2차원 배열과 headerLine을 이용해 JTable객체 생성
			DefaultTableModel model = new DefaultTableModel(data, columnName);
			JTable viewTable = new JTable(model);
			viewTable.setShowGrid(true);
			viewTable.setShowVerticalLines(true);
			
			return viewTable;
			
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, "SQL 실행 오류", "오류 메시지", JOptionPane.WARNING_MESSAGE);
		}
		return null;		
	}
	
	private void viewAll() {	
		JFrame tableJf = new JFrame("전체 테이블 출력");
		JPanel stock = new JPanel();
		JPanel mini;
		JLabel label;
		JScrollPane tableData;
		String tableName[] = {"Movie", "Theater", "Schedule", "Seat", "Member", "Reservation", "Ticket"};
		
		for (int i = 0; i < 7; ++i) {
			//getTable 메소드를 이용해 테이블을 받아오기
			tableData = new JScrollPane(getTable(tableName[i], ""), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			tableData.setPreferredSize(new Dimension(500, 200));
			
			//label 붙이고 jFrame에 추가하기
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
	
	// 아래는 회원 기능 구현
	
	// 모든 영화에 대한 조회 기능, 아무 조건도 입력되지 않는 것도 계산해야함...
	public void searchMovie() {
		String sql = "SELECT * FROM Movie WHERE";
		String[] expression = {" movie_name == ", " director == ", " actor == ", " genre == "};  
		String inputText = JOptionPane.showInputDialog("검색 조건을 영화명, 감독명, 배우명, 장르 순으로 입력해 주세요"
				+ "\nex)'봉준호'의 영화 조회"
				+ "\n없음 '봉준호' 없음 없음");
		String[] condition = inputText.split(" ");
		if (condition.length != 4) {
			JOptionPane.showMessageDialog(null, "양식에 맞춰 입력해 주세요!", "오류 메시지", JOptionPane.WARNING_MESSAGE);
			return;
		}
		for (int i = 0; i < 4; ++i) {
			if (condition[i] == "없음") continue;
			sql += expression[i] + condition[i] + " and";
		}
		if (sql.charAt(sql.length() - 1) == 'd') {
			sql = sql.substring(0, sql.length() - 4);
		}
		else {
			sql = "SELECT * FROM Movie";
		}
		sql += ";";
		JScrollPane tableData = new JScrollPane(getTable("Movie", sql), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JFrame tableJf = new JFrame("영화 조회");
		tableJf.add(tableData);
		tableJf.setVisible(true);
	}

	//
	
	public static void main(String[] args) { 
		JC19011051M ui = new JC19011051M();
	}

	
}
	


