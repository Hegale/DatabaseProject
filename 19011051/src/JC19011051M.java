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
	//회원의 추가기능을 위한 패널
	JPanel moviePanel;	
	
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
	//회원 페이지 userPanel의 버튼
	JButton btnSearchMovie, btnReservation, btnMovieReservation;
	

	JScrollPane scrollPane; // txtResul를 넣어줄 JScrollPane
	JTextArea txtResult; // 결과값들 저장할 JTextArea (Center에 들어갈 예정)
	
	JTable movieTable; // 유저 페이지에서 영화 목록을 보여주는 table
	
	//JOptionpane 초기 회원ID 입력창 
	JOptionPane inputUserIdPane;
	
	// 예매 시점 현재 날짜를 임의로 2021년 5월 5일로 설정
	String nowDate = "2021.05.05";
	
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
		
		btnSearchMovie = new JButton("영화 조회/예매");
		btnReservation = new JButton("나의 예매 현황");
		btnMovieReservation = new JButton("선택한 영화 예매");

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
		
		//관리자 페이지
		//adminPanel 레이아웃
		adminPanel.setVisible(false);
		adminPanel.add(btnReturnMainFromAdmin);
		adminPanel.add(btnResetDB);
		adminPanel.add(btnInsert);
		adminPanel.add(btnDelete);
		adminPanel.add(btnModify);
		adminPanel.add(btnViewAll);
		
		//회원 페이지
		//userPanel 레이아웃
		userPanel.setVisible(false);
		userPanel.add(btnSearchMovie);
		userPanel.add(btnReservation);
		moviePanel.add(btnMovieReservation);
		
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
		
		btnSearchMovie.addActionListener(this);
		btnReservation.addActionListener(this);		
		
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
		
		else if(e.getSource() == btnInsert) { // 관리자 패널의 insert 버튼이 눌린 경우
			String tableName = JOptionPane.showInputDialog("입력할 테이블 이름을 입력하시오");
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

                    int result = JOptionPane.showConfirmDialog(null, myPanel,"Movie 테이블의 속성값들을 입력하세요.", JOptionPane.OK_CANCEL_OPTION);

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
                        JOptionPane.showMessageDialog(null, "SQL Exception\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                    }
                }	
			}catch(NumberFormatException e3) {
                JOptionPane.showMessageDialog(null, "잘못된 입력입니다\n"+e3.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
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
			JOptionPane.showMessageDialog(null, e, "오류 메시지", JOptionPane.WARNING_MESSAGE);
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
		insertMovieSQL[0] = "INSERT INTO Movie VALUES(1, '신과함께-죄와 벌', '139분', '12세 이상 관람가', '김용화', '하정우, 차태현, 주지훈, 김향기', '판타지', '주호민 작가의 웹툰 신과함께의 영화화 작품. 1부의 죄와 벌 편은 원작의 저승 편을 기반으로 각색되었다. 주 내용은 김자홍의 7번의 재판과 원귀가 이승에서 벌이고 있는 소동을 나눠 다루고 있다.', '2021.01.07');"; 
		insertMovieSQL[1] = "INSERT INTO Movie VALUES(2, '엽기적인그녀', '137분', '15세 이상 관람가', '곽재용', '전지현, 차태현', '코미디, 드라마, 로맨스', '1999년 8월부터 당시 대학생이던 견우74(본명 김호식)란 ID를 쓴 네티즌이 PC통신 나우누리 유머란에서 연재하여 엄청난 호평을 받았던 동명의 소설을 바탕으로 한 두 남녀 대학생의 엽기발랄한 러브 스토리.', '2021.02.24');";
		insertMovieSQL[2] = "INSERT INTO Movie VALUES(3, '미나리', '115분', '12세 이상 관람가', '정이삭', '스티븐 연, 한예리, 윤여정, 앨런 김', '드라마', '1980년대 미국으로 이민간 한국 이민자 가족이 시골에서 농장을 만드는 이야기를 다룬 미국 영화다.', '2021.03.03');";
		insertMovieSQL[3] = "INSERT INTO Movie VALUES(4, '죽은 시인의 사회', '128분', '12세 이상 관람가', '피터 위어', '로빈 윌리엄스', '드라마', '1959년 버몬트의 개신교계[9] 귀족 학교 분위기가 물씬 풍기는 사립학교에서 벌어지는 일련의 교육 활동을 주 소재로 하고 있으며, 사실 이런 내용은 이 영화의 각본가 톰 슐만의 자전적인 경험을 바탕으로 하고 있다.', '2021.04.01');";
		insertMovieSQL[4] = "INSERT INTO Movie VALUES(5, '분노의 질주:더 얼티메이트', '142분', '12세 이상 관람가', '저스틴 린', '빈 디젤, 존 시나, 성 강, 미셸 로드리게즈', '액션', '분노의 질주 시리즈의 9번째 작품이자 시리즈의 마지막을 장식할 트릴로지의 첫 편. 이전작에서 사망해 퇴장했던 한이 오랜만에 복귀하였고 주인공 도미닉의 동생인 제이콥 토레토 역으로 존 시나가 출연한다.', '2021.05.19');";
		insertMovieSQL[5] = "INSERT INTO Movie VALUES(6, '발신제한', '94분', '15세 이상 관람가', '김창주', '조우진, 이재인, 진경, 김지호', '드라마', '한국의 스릴러 영화. 더 테러 라이브, 표적, 끝까지 간다, 킹덤등을 비롯해 다수의 한국영화의 편집을 맡았던 김창주의 장편 연출 데뷔작이며, 스페인 영화 <레트리뷰션: 응징의 날> (2015)의 한국 리메이크작이다.', '2021.06.23');";
		insertMovieSQL[6] = "INSERT INTO Movie VALUES(7, '모가디슈', '121분', '15세 이상 관람가', '류승완', '김윤석, 조인성, 허준호, 구교환', '액션', '내전으로 고립된 낯선 도시, 모가디슈 지금부터 우리의 목표는 오로지 생존이다! 대한민국이 UN가입을 위해 동분서주하던 시기 1991년 소말리아의 수도 모가디슈에서는 일촉즉발의 내전이 일어난다.', '2021.07.28');";
		insertMovieSQL[7] = "INSERT INTO Movie VALUES(8, '싱크홀', '113분', '12세 이상 관람가', '김지훈', '차승원, 김성균, 이광수, 권소현', '코미디, 재난, 드라마', '싱크홀 현상을 주소재로 다룬 영화이다.김지훈 감독의 연출 작품이며, 엑시트 외에는 국내에서 잘 나오지 않던 코미디와 재난물을 섞어 놓은 스타일의 작품이다.', '2021.08.11');";
		insertMovieSQL[8] = "INSERT INTO Movie VALUES(9, '기적', '117분', '12세 이상 관람가', '이장훈', '박정민, 이성민, 윤아, 이수경, 김강훈', '드라마', '오갈 수 있는 길은 기찻길밖에 없지만 정작 기차역은 없는 마을. 오늘부로 청와대에 딱 54번째 편지를 보낸 준경(박정민)의 목표는 단 하나! 바로 마을에 기차역이 생기는 것이다.', '2021.09.15');";
		insertMovieSQL[9] = "INSERT INTO Movie VALUES(10, '베놈 2:렛 데어 비 카니지', '97분', '15세 이상 관람가', '앤디 서키스', '톰하디, 우디 해럴슨', '액션, SF', '베놈 실사영화 시리즈의 2번째 작품이자, 소니 스파이더맨 유니버스의 2번째 작품이다.', '2021.10.13');";
		insertMovieSQL[10] = "INSERT INTO Movie VALUES(11, '연애빠진 로맨스', '95분', '15세 이상 관람가', '정가영', '전종서, 손석구, 공민정, 김슬기, 배유람', '로맨스', '일도 연애도 마음대로 되지 않는 스물아홉 ‘자영’(전종서). 전 남친과의 격한 이별 후 호기롭게 연애 은퇴를 선언했지만 참을 수 없는 외로움에 못 이겨 최후의 보루인 데이팅 어플로 상대를 검색한다.', '2021.11.24');";
		insertMovieSQL[11] = "INSERT INTO Movie VALUES(12, '스파이더맨:노 웨이 홈', '148분', '12세 이상 관람가', '존 왓츠', '톰 홀랜드, 젠데이아 콜먼, 베네딕트 컴버배치, 제이콥 배덜런', '액션, 모험, SF', '마블 시네마틱 유니버스의 27번째 장편 영화. 또한 페이즈 4의 4번째 영화이자 마블 스튜디오 스파이더맨 시리즈의 3번째 작품이자 마블 스튜디오 스파이더맨 시리즈의 첫번째 트릴로지인 홈커밍 트릴로지의 마지막 작품이다.', '2021.12.15');";
		
		//설정한 string을 실행함
		executeSQL(initSQL);
		executeSQL(createSQL);
		executeSQL(insertMovieSQL);
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
	
	// 모든 영화에 대한 조회 기능
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
			if (condition[i].equals("없음")) continue;
			sql += expression[i] + condition[i] + " and";
		}
		if (sql.charAt(sql.length() - 1) == 'd') {
			sql = sql.substring(0, sql.length() - 4);
		}
		else {
			sql = "SELECT * FROM Movie";
		}
		sql += ";";
		
		//검색 조건에 부합하는 테이블을 담은 창 띄우기
		JFrame tableJf = new JFrame("영화 조회");
		tableJf.setLocation(400, 300);
		tableJf.setSize(700, 450);
		
		//영화 리스트를 테이블로 불러오기
		movieTable = getTable("Movie", sql);
		movieTable.addMouseListener(this);
		
		JScrollPane tableData = new JScrollPane(movieTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tableData.setPreferredSize(new Dimension(500, 200));

		JPanel mini = new JPanel();
		mini.setLayout(new BoxLayout(mini, BoxLayout.Y_AXIS));
		JLabel label = new JLabel("검색한 조건에 부합하는 영화 조회");
		mini.add(label);
		mini.add(tableData);
		mini.add(btnMovieReservation);
		
		tableJf.add(mini);
		tableJf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tableJf.setVisible(true);
	}
	
	// searchMovie에서 조회한 영화에 대한 예매 기능
	public void movieReservation(int movie_id) {
		System.out.println("하하~~");
	}
	
	public void mouseClicked(MouseEvent e) {
		JTable table = (JTable)e.getComponent();
		TableModel model = (TableModel)table.getModel();
		int column = table.getSelectedColumn();
		int row = table.getSelectedRow();
		System.out.println("헤헤");
		
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
	


