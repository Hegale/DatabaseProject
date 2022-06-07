import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.text.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;
import java.awt.Dimension;
import java.awt.event.*;
import java.sql.*;




public class JC19011051M extends JFrame implements ActionListener, MouseListener{
	JButton btnAdmin, btnUser;
	JPanel mainPanel, adminPanel, userPanel;
	JPanel reservationPanel;
	
	// DatabaseConnection
	String driver, URL, user, password, sql;
	Connection con;
	PreparedStatement pstmt;
	Statement stmt;
    ResultSet rs;
	
	
	//메인으로 돌아오는 버튼
	JButton btnReturnMainFromAdmin, btnReturnMainFromUser, btnReturnMainFromProfessor, btnReturnMainFromStudent;
	//관리자 페이지 adminPanel의 버튼
	JButton btnResetDB, btnInsert, btnDelete, btnModify, btnViewAll;
	//회원 페이지 userPanel의 버튼
	JButton btnSearchMovie, btnReservation, btnMovieReservation, delete_reserv_btn, update_movie_btn, update_schedule_btn;


	JScrollPane scrollPane; // txtResul를 넣어줄 JScrollPane
	JTextArea txtResult; // 결과값들 저장할 JTextArea (Center에 들어갈 예정)
	
	JTable movieTable; // 유저 페이지에서 영화 목록을 보여주는 table
	JTable reservationTable; // 유저 페이지에서 영화 목록을 보여주는 table
	
	//JOptionpane 초기 회원ID 입력창 
	JOptionPane inputUserIdPane;
	
	// 예매 시점 현재 날짜를 임의로 2021년 5월 5일로 설정
	String nowDate = "2021.05.05";
	int User_id = -1; //회원 아이디 저장 변수
	
	
	// 테이블을 선택했을 때 해당 위치의 값을 받아오는 변수들
	int row = -1, column = -1, movie_id = -1;
	int[] rows;
	ArrayList<Integer> selected_IDs = new ArrayList<Integer>();
	int ticket_price = 8000; //티켓 가격
	
	public JC19011051M() {
	     String Driver="";
	     String 
	url="jdbc:mysql://localhost:3306/madang?&serverTimezone=Asia/Seoul"; 
	     String userid="madang";
	     String pwd="madang";
	// 접속변수를 초기화한다. url은 자바 드라이버 이름, 호스트명(localhost), 포트번호를 입력한다
	// userid는 관리자(madang), pwd는 사용자의 비밀번호(madang)를 입력한다.    
	     try { /* 드라이버를 찾는 과정 */
	       Class.forName("com.mysql.cj.jdbc.Driver");   
	       System.out.println("드라이버 로드 성공");
	     } catch(ClassNotFoundException e) {
	         e.printStackTrace();
	      }
	// Class.forName()으로 드라이버를 로딩한다. 드라이버 이름을 Class.forName에 입력한다.      
	     try { /* 데이터베이스를 연결하는 과정 */
	       System.out.println("데이터베이스 연결 준비...");	
	       con=DriverManager.getConnection(url, userid, pwd);
	       System.out.println("데이터베이스 연결 성공");
	     } catch(SQLException e) {
	         e.printStackTrace();
	       }
	}
	
	private void setFrame() {
		setTitle("18011575 정상헌 / 19011051 김주연");
		initLayout();
		setVisible(true);
		setBounds(200, 50, 700, 500); //x좌표 ,y좌표 , 가로길이, 세로길이
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void initLayout() {
			
		//JButton
		btnAdmin = new JButton("관리자");
		btnUser = new JButton("회원");
		
		btnReturnMainFromAdmin = new JButton("메인으로");
		btnReturnMainFromUser = new JButton("메인으로");
		
		btnResetDB = new JButton("DB 초기화");
		btnInsert = new JButton("입력");
		btnDelete = new JButton("삭제");
		btnModify = new JButton("변경");
		btnViewAll = new JButton("전체 테이블 조회");
		
		btnSearchMovie = new JButton("영화 조회/예매");
		btnReservation = new JButton("나의 예매 현황");
		btnMovieReservation = new JButton("선택한 영화 예매");
		
		delete_reserv_btn = new JButton("예매 취소");
		update_movie_btn = new JButton("영화 변경");
		update_schedule_btn = new JButton("일정 변경");
		
		//JPanel
		mainPanel = new JPanel();
		adminPanel = new JPanel();
		userPanel = new JPanel();
		reservationPanel = new JPanel();
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
		userPanel.add(btnReturnMainFromUser);
		reservationPanel.add(delete_reserv_btn); 
		reservationPanel.add(update_movie_btn); 
		reservationPanel.add(update_schedule_btn); 
		
		//ScrollPane, TextArea
		txtResult = new JTextArea();
		txtResult.setEditable(false);
		scrollPane = new JScrollPane(txtResult);
		
		//버튼 이벤트
        btnAdmin.addActionListener(this);
        btnUser.addActionListener(this);
        btnReturnMainFromAdmin.addActionListener(this);
        btnReturnMainFromUser.addActionListener(this);
        
        btnResetDB.addActionListener(this);
		btnInsert.addActionListener(this);
		btnDelete.addActionListener(this);
		btnModify.addActionListener(this);
		btnViewAll.addActionListener(this);
		
		btnSearchMovie.addActionListener(this);
		btnReservation.addActionListener(this);		
		btnMovieReservation.addActionListener(this);
		delete_reserv_btn.addActionListener(this);
		update_schedule_btn.addActionListener(this);
		update_movie_btn.addActionListener(this);
		
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
				
				User_id = Integer.parseInt(id);
				// id 잘못 입력시 에러 표시!
					
				// member 테이블에서 memberid 회원 찾는 쿼리
				String query = "SELECT * FROM member WHERE member_id = " + id + ";";
				try {
					stmt = con.createStatement();
					rs = stmt.executeQuery(query);
					
					//존재하지 않는 회원 id인 경우, 접근 허용하지 않음
					if (!rs.next()) {
						JOptionPane.showMessageDialog(null, "존재하지 않는 회원입니다", "오류", JOptionPane.ERROR_MESSAGE);
					}
					else {
						add("North", userPanel);
						add("Center", scrollPane);
						mainPanel.setVisible(false);
						userPanel.setVisible(true);
					}
				} catch(SQLException e1) {
					e1.printStackTrace();
				}				
			}
		}
		
		else if(e.getSource() == btnReturnMainFromAdmin) { //메인으로 돌아가기
			adminPanel.setVisible(false);
			mainPanel.setVisible(true);
			
			txtResult.setText("");
		}
		
		else if (e.getSource() == btnReturnMainFromUser) {
			userPanel.setVisible(false);
			mainPanel.setVisible(true);
			
			txtResult.setText("");
		}
		
		else if(e.getSource() == btnInsert) { // 관리자 패널의 insert 버튼이 눌린 경우
			String tableName = JOptionPane.showInputDialog("입력할 테이블 이름을 입력하시오. "
					+ "(Member, Movie, Reservation, Schedule, Seat, Theater, Ticket)");
			
			// 테이블 입력 잘못입력하면 에러 출력 
			if (true) {
				ArrayList<String> tableNameArray = new ArrayList<>(Arrays.asList("Member", "Movie", "Reservation", "Schedule", "Seat", "Theater", "Ticket"));
				if (!tableNameArray.contains(tableName)) {
					JOptionPane.showMessageDialog(null, "잘못된 입력입니다\n", "오류", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			
			
			// insert Member
			try {
				if(tableName.equals("Member")) {

                    JTextField member_id = new JTextField(4);
                    JTextField member_name = new JTextField(4);
                    JTextField phone = new JTextField(4);
                    JTextField mail = new JTextField(4);
                    JPanel insertMemberPanel = new JPanel();
                    
                    insertMemberPanel.add(new JLabel("MemberID:"));
                    insertMemberPanel.add(member_id);
                    insertMemberPanel.add(Box.createHorizontalStrut(10)); // a spacer
                    insertMemberPanel.add(new JLabel("MemberName:"));
                    insertMemberPanel.add(member_name);
                    insertMemberPanel.add(Box.createHorizontalStrut(10)); // a spacer
                    insertMemberPanel.add(new JLabel("Phone:"));
                    insertMemberPanel.add(phone);
                    insertMemberPanel.add(Box.createHorizontalStrut(10)); // a spacer
                    insertMemberPanel.add(new JLabel("Mail:"));
                    insertMemberPanel.add(mail);

                    JOptionPane.showConfirmDialog(null, insertMemberPanel,"Member 테이블의 속성값들을 입력하세요.", JOptionPane.OK_CANCEL_OPTION);

                    int member_id_int = Integer.parseInt(member_id.getText());
                    String member_name_str = (member_name.getText());
                    String phone_str = (phone.getText());
                    String mail_str = (mail.getText());

                    try {

                        Statement stmt = con.createStatement();

                        String insertSQL = "insert into Member (member_id, member_name, phone, mail)"
                                + " values("+member_id_int+",'"+member_name_str+"','"+phone_str+"', '"+mail_str+"');";
                        stmt.executeUpdate(insertSQL);

                    }catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "SQL Exception\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                    }
                }	
			}catch(NumberFormatException e3) {
                JOptionPane.showMessageDialog(null, "잘못된 입력입니다\n"+e3.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }
			
			// insert Movie
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
                    JTextField release_date = new JTextField(4);
                    JPanel insertMoviePanel = new JPanel();
                    
                    insertMoviePanel.add(new JLabel("MovieID:"));
                    insertMoviePanel.add(movie_id);
                    insertMoviePanel.add(Box.createHorizontalStrut(10)); // a spacer
                    
                    insertMoviePanel.add(new JLabel("MovieName:"));
                    insertMoviePanel.add(movie_name);
                    insertMoviePanel.add(Box.createHorizontalStrut(10)); // a spacer
                    insertMoviePanel.add(new JLabel("ScreenTime:"));
                    insertMoviePanel.add(screentime);
                    insertMoviePanel.add(Box.createHorizontalStrut(10)); // a spacer
                    insertMoviePanel.add(new JLabel("Rating:"));
                    insertMoviePanel.add(rating);
                    insertMoviePanel.add(Box.createHorizontalStrut(10)); // a spacer
                    insertMoviePanel.add(new JLabel("Director:"));
                    insertMoviePanel.add(director);
                    insertMoviePanel.add(Box.createHorizontalStrut(10)); // a spacer
                    insertMoviePanel.add(new JLabel("Actor:"));
                    insertMoviePanel.add(actor);
                    insertMoviePanel.add(Box.createHorizontalStrut(10)); // a spacer
                    insertMoviePanel.add(new JLabel("Genre:"));
                    insertMoviePanel.add(genre);
                    insertMoviePanel.add(Box.createHorizontalStrut(10)); // a spacer
                    insertMoviePanel.add(new JLabel("Introduce:"));
                    insertMoviePanel.add(introduce);
                    insertMoviePanel.add(Box.createHorizontalStrut(10)); // a spacer
                    insertMoviePanel.add(new JLabel("ReleaseDate:"));
                    insertMoviePanel.add(release_date);

                    JOptionPane.showConfirmDialog(null, insertMoviePanel,"Movie 테이블의 속성값들을 입력하세요.", JOptionPane.OK_CANCEL_OPTION);

                    int movie_id_int = Integer.parseInt(movie_id.getText());
                    String movie_name_str = (movie_name.getText());
                    String screentime_str = (screentime.getText());
                    String rating_str = (rating.getText());
                    String director_str = (director.getText());
                    String actor_str = (actor.getText());
                    String genre_str = (genre.getText());
                    String introduce_str = (introduce.getText());
                    String release_str = (release_date.getText());

                    try {

                        Statement stmt = con.createStatement();

                        String insertSQL = "insert into Movie (movie_id, movie_name, screentime, rating, director, actor, genre, introduce, release_date) values("
                        		+ movie_id_int+", '"+movie_name_str+"', '"+screentime_str+"', '"+rating_str+"', '"+director_str+"', '"
                        		+actor_str+"', '"+genre_str+"', '"+introduce_str+"', '"+release_str+"');";
                        stmt.executeUpdate(insertSQL);

                    }catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "SQL Exception\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                    }
                }	
			}catch(NumberFormatException e3) {
                JOptionPane.showMessageDialog(null, "잘못된 입력입니다\n"+e3.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }
			
			// insert Reservation
			try {
				
				if(tableName.equals("Reservation")) {

                    JTextField reservation_id = new JTextField(4);
                    JTextField pay_method = new JTextField(4);
                    JTextField pay_status = new JTextField(4);
                    JTextField pay_amount = new JTextField(4);
                    JTextField pay_date = new JTextField(4);
                    JTextField member_id = new JTextField(4);
                    JPanel insertReservationPanel = new JPanel();
                    
                    insertReservationPanel.add(new JLabel("Reservation ID : "));
                    insertReservationPanel.add(reservation_id);
                    insertReservationPanel.add(Box.createHorizontalStrut(10)); // a spacer
                    insertReservationPanel.add(new JLabel("Pay Method : "));
                    insertReservationPanel.add(pay_method);
                    insertReservationPanel.add(Box.createHorizontalStrut(10)); // a spacer
                    insertReservationPanel.add(new JLabel("Pay Status : "));
                    insertReservationPanel.add(pay_status);
                    insertReservationPanel.add(Box.createHorizontalStrut(10)); // a spacer
                    insertReservationPanel.add(new JLabel("Pay Amount : "));
                    insertReservationPanel.add(pay_amount);
                    insertReservationPanel.add(Box.createHorizontalStrut(10)); // a spacer
                    insertReservationPanel.add(new JLabel("Pay Date : "));
                    insertReservationPanel.add(pay_date);
                    insertReservationPanel.add(Box.createHorizontalStrut(10)); // a spacer
                    insertReservationPanel.add(new JLabel("Member ID : "));
                    insertReservationPanel.add(member_id);

                    JOptionPane.showConfirmDialog(null, insertReservationPanel,"Reservation 테이블의 속성값들을 입력하세요.", JOptionPane.OK_CANCEL_OPTION);

                    int reservation_id_int = Integer.parseInt(reservation_id.getText());
                    String pay_method_str = (pay_method.getText());
                    String pay_status_str = (pay_status.getText());
                    String pay_amount_str = (pay_amount.getText());
                    String pay_date_str = (pay_date.getText());
                    int member_id_int = Integer.parseInt(member_id.getText());

                    try {

                        Statement stmt = con.createStatement();

                        String insertSQL = "insert into Reservation (reservation_id, pay_method, pay_status, pay_amount, pay_date, member_id)"
                                + " values("+reservation_id_int+",'"+pay_method_str+"','"+pay_status_str+"', '"
                        		+pay_amount_str+"' , '"+pay_date_str+"', '"+member_id_int+"');";

                        stmt.executeUpdate(insertSQL);

                    }catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "SQL Exception\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                    }
                }	
			}catch(NumberFormatException e3) {
                JOptionPane.showMessageDialog(null, "잘못된 입력입니다\n"+e3.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }
			
			// insert Schedule
			try {
				if(tableName.equals("Schedule")) {

                    JTextField schedule_id = new JTextField(4);
                    JTextField date = new JTextField(4);
                    JTextField day = new JTextField(4);
                    JTextField round = new JTextField(4);
                    JTextField time = new JTextField(4);
                    JTextField movie_id= new JTextField(4);
                    JTextField theater_id = new JTextField(4);
                    JPanel insertSchedulePanel = new JPanel();
                    
                    insertSchedulePanel.add(new JLabel("Schedule ID : "));
                    insertSchedulePanel.add(schedule_id);
                    insertSchedulePanel.add(Box.createHorizontalStrut(10));
                    insertSchedulePanel.add(new JLabel("Date : "));
                    insertSchedulePanel.add(date);
                    insertSchedulePanel.add(Box.createHorizontalStrut(10));
                    insertSchedulePanel.add(new JLabel("Day : "));
                    insertSchedulePanel.add(day);
                    insertSchedulePanel.add(Box.createHorizontalStrut(10));
                    insertSchedulePanel.add(new JLabel("Round : "));
                    insertSchedulePanel.add(round);
                    insertSchedulePanel.add(Box.createHorizontalStrut(10));
                    insertSchedulePanel.add(new JLabel("Time : "));
                    insertSchedulePanel.add(time);
                    insertSchedulePanel.add(Box.createHorizontalStrut(10));
                    insertSchedulePanel.add(new JLabel("Movie ID : "));
                    insertSchedulePanel.add(movie_id);
                    insertSchedulePanel.add(Box.createHorizontalStrut(10));
                    insertSchedulePanel.add(new JLabel("theater ID : "));
                    insertSchedulePanel.add(theater_id);

                    JOptionPane.showConfirmDialog(null, insertSchedulePanel,"Schedule 테이블의 속성값들을 입력하세요.", JOptionPane.OK_CANCEL_OPTION);

                    int schedule_id_int = Integer.parseInt(schedule_id.getText());
                    String date_str = (date.getText());
                    String day_str = (day.getText());
                    String round_str = (round.getText());
                    String time_str = (time.getText());
                    int movie_id_int = Integer.parseInt(movie_id.getText());
                    int theater_id_int = Integer.parseInt(theater_id.getText());
                                                        
                    try {
                        stmt = con.createStatement();

                        String insertSQL = "insert into Schedule (schedule_id, date, day, round, time, movie_id, theater_id)"
                                + " values("+schedule_id_int+",'"+date_str+"','"+day_str+"', '"+round_str+"' , '"
                        		+time_str+"', '"+movie_id_int+"', '"+theater_id_int+"');";
                        stmt.executeUpdate(insertSQL);

                    }catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "SQL Exception\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                    }
                    
                    
                  //theater_id_int에 해당하는 theater가 사용이 'N'이면.. 'Y'로 바꿔줘야함
                    
                    String theaterQuery = "select * from theater where theater_use = 'N' and theater_id = " + theater_id.getText() + ";";
                    System.out.println(theaterQuery);
                    
                    try{
                    	stmt = con.createStatement();
                    	rs = stmt.executeQuery(theaterQuery);
                    	if (rs.next()) {
                    		Statement stmt2 = con.createStatement();
                    		stmt2.executeUpdate("UPDATE Theater SET theater_use = 'Y' WHERE theater_id = " + theater_id.getText() + ";");
                    	}               
                    } catch(SQLException e2) {
                    	e2.printStackTrace();
                    }
                    
                    
                }	
			}catch(NumberFormatException e3) {
                JOptionPane.showMessageDialog(null, "잘못된 입력입니다\n"+e3.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			// insert Seat
			try {
				
				if(tableName.equals("Seat")) {

                    JTextField seat_id = new JTextField(4);
                    JTextField seat_use = new JTextField(4);
                    JTextField theater_id = new JTextField(4);
                    JPanel insertSeatPanel = new JPanel();
                    
                    insertSeatPanel.add(new JLabel("Seat ID : "));
                    insertSeatPanel.add(seat_id);
                    insertSeatPanel.add(Box.createHorizontalStrut(10));
                    insertSeatPanel.add(new JLabel("Seat Use : "));
                    insertSeatPanel.add(seat_use);
                    insertSeatPanel.add(Box.createHorizontalStrut(10));
                    insertSeatPanel.add(new JLabel("Theater ID : "));
                    insertSeatPanel.add(theater_id);
                   
                    JOptionPane.showConfirmDialog(null, insertSeatPanel,"Seat 테이블의 속성값들을 입력하세요.", JOptionPane.OK_CANCEL_OPTION);

                    int seat_id_int = Integer.parseInt(seat_id.getText());
                    String seat_use_str = seat_use.getText();
                    int theater_id_int = Integer.parseInt(theater_id.getText());

                    try {

                        Statement stmt = con.createStatement();

                        String insertSQL = "insert into Seat (seat_id, seat_use, theater_id)"
                                + " values("+seat_id_int+",'"+seat_use_str+"','"+theater_id_int+"');";
                        stmt.executeUpdate(insertSQL);

                    }catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "SQL Exception\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                    }
                }	
			}catch(NumberFormatException e3) {
                JOptionPane.showMessageDialog(null, "잘못된 입력입니다\n"+e3.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }
			
			
			// insert Theater
			try {
				
				if(tableName.equals("Theater")) {

                    JTextField theater_id = new JTextField(4);
                    JTextField seat_num = new JTextField(4);
                    JTextField theater_use = new JTextField(4);
                    JPanel myPanel = new JPanel();
                    
                    myPanel.add(new JLabel("Theater ID : "));
                    myPanel.add(theater_id);
                    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
                    myPanel.add(new JLabel("Seat Num : "));
                    myPanel.add(seat_num);
                    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
                    myPanel.add(new JLabel("Theater Use : "));
                    myPanel.add(theater_use);
                    
                    int result = JOptionPane.showConfirmDialog(null, myPanel,"Movie 테이블의 속성값들을 입력하세요.", JOptionPane.OK_CANCEL_OPTION);

                    int theater_id_int = Integer.parseInt(theater_id.getText());
                    int seat_num_int = Integer.parseInt(seat_num.getText());
                    String theater_use_str = theater_use.getText();
                    
                    try {
                        Statement stmt = con.createStatement();

                        String insertSQL = "insert into Theater (theater_id, seat_num, theater_use)"
                                + " values("+theater_id_int+",'"+seat_num_int+"','"+theater_use_str+"');";
                        stmt.executeUpdate(insertSQL);

                    }catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "SQL Exception\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                    }
                }	
			}catch(NumberFormatException e3) {
                JOptionPane.showMessageDialog(null, "잘못된 입력입니다\n"+e3.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }
			
			
			// insert Ticket
			try {
				
				if(tableName.equals("Ticket")) {

                    JTextField ticket_id = new JTextField(4);
                    JTextField issue = new JTextField(4);
                    JTextField std_price = new JTextField(4);
                    JTextField price = new JTextField(4);
                    JTextField theater_id = new JTextField(4);
                    JTextField schedule_id = new JTextField(4);
                    JTextField seat_id = new JTextField(4);
                    JTextField reservation_id = new JTextField(4);
                    JPanel myPanel = new JPanel();
                    
                    myPanel.add(new JLabel("Ticket ID:"));
                    myPanel.add(ticket_id);
                    myPanel.add(Box.createHorizontalStrut(10));
                    myPanel.add(new JLabel("Issue : "));
                    myPanel.add(issue);
                    myPanel.add(Box.createHorizontalStrut(10));
                    myPanel.add(new JLabel("STD Price : "));
                    myPanel.add(std_price);
                    myPanel.add(Box.createHorizontalStrut(10));
                    myPanel.add(new JLabel("Price : "));
                    myPanel.add(price);
                    myPanel.add(Box.createHorizontalStrut(10));
                    myPanel.add(new JLabel("Theater ID : "));
                    myPanel.add(theater_id);
                    myPanel.add(Box.createHorizontalStrut(10));
                    myPanel.add(new JLabel("Schedule ID : "));
                    myPanel.add(schedule_id);
                    myPanel.add(Box.createHorizontalStrut(10));
                    myPanel.add(new JLabel("Seat ID : "));
                    myPanel.add(seat_id);
                    myPanel.add(Box.createHorizontalStrut(10));
                    myPanel.add(new JLabel("Reservation ID : "));
                    myPanel.add(reservation_id);

                    JOptionPane.showConfirmDialog(null, myPanel,"Ticket 테이블의 속성값들을 입력하세요.", JOptionPane.OK_CANCEL_OPTION);

                    int ticket_id_int = Integer.parseInt(ticket_id.getText());
                    String issue_str = (issue.getText());
                    String std_price_str = (std_price.getText());
                    String price_str = (price.getText());
                    int theater_id_int = Integer.parseInt(theater_id.getText());
                    int schedule_id_int = Integer.parseInt(schedule_id.getText());
                    int seat_id_int = Integer.parseInt(seat_id.getText());
                    int reservation_id_int = Integer.parseInt(reservation_id.getText());

                    try {
                        Statement stmt = con.createStatement();

                        String insertSQL = "insert into Ticket (ticket_id, issue, std_price, price, theater_id, schedule_id, seat_id, reservation_id)"
                                + " values("+ticket_id_int+",'"+issue_str+"','"+std_price_str+"', '"+price_str+"', "
                        		+theater_id_int+", "+schedule_id_int+", "+seat_id_int+", "+reservation_id_int+");";
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
			
			JTextField tableName = new JTextField(8);
            JTextField where = new JTextField(8);
            JPanel deletePanel = new JPanel();

            deletePanel.add(new JLabel("어떤 테이블을 삭제하시겠습니까?"));
            deletePanel.add(tableName);
            deletePanel.add(Box.createHorizontalStrut(2));
            deletePanel.add(new JLabel("조건식을 써주세요"));
            deletePanel.add(where);

            try {
                JOptionPane.showConfirmDialog(null, deletePanel,"입력!!!", JOptionPane.OK_CANCEL_OPTION);


                String tableName_str = tableName.getText();
                String where_str = where.getText();
                Statement stmt = con.createStatement();
                String deleteSQL = "Delete from " + tableName_str + " where " + where_str + ";";

                stmt.execute("set sql_safe_updates = 0;");
                stmt.executeUpdate(deleteSQL);

            }
            catch (SQLException e1) {
                JOptionPane.showMessageDialog(null, "잘못된 입력입니다.\n"+e1.getMessage(), "입력 오류", JOptionPane.ERROR_MESSAGE);
            }
            catch (Exception e2) {
            }
            
            // 삭제할 때 상영관이 완전히 비게 되면 N으로 바꿔줘야함... (모든 상영관에 대해 비었는지 점검??)
			
		}
		else if(e.getSource() == btnModify) {
			JTextField tableName = new JTextField(8);
            JTextField set = new JTextField(8);
            JTextField where = new JTextField(8);
            JPanel modifyPanel = new JPanel();

            modifyPanel.add(new JLabel("속성 변경할 테이블:"));
            modifyPanel.add(tableName);
            modifyPanel.add(Box.createHorizontalStrut(2));
            modifyPanel.add(new JLabel("변경할내용 ex) member_name = '둘리둘리' :"));
            modifyPanel.add(set);
            modifyPanel.add(Box.createHorizontalStrut(2));
            modifyPanel.add(new JLabel("조건식 ex) member_id = 1 :"));
            modifyPanel.add(where);

            JOptionPane.showConfirmDialog(null, modifyPanel,"입력하시오", JOptionPane.OK_CANCEL_OPTION);

            String tableName_str = (tableName.getText());
            String set_str = (set.getText());
            String where_str = (where.getText());


            try {
                Statement stmt = con.createStatement();
                String updateSQL = "Update " + tableName_str + " Set " + set_str + " where " + where_str + ";";
                System.out.println(updateSQL);
                stmt.executeUpdate(updateSQL);

            }catch (SQLException e1) {
                JOptionPane.showMessageDialog(null, "입력 오류입니다.\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
			
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
			showUserReservation();
			
		}
		else if (e.getSource() == btnMovieReservation) {
			movieReservation();
		}
		
		else if (e.getSource() == delete_reserv_btn) {
			deleteReserve();
		}
		else if (e.getSource() == update_movie_btn) {
			changeMovie();
		}
		else if (e.getSource() == update_schedule_btn) {
			updateSchedule();
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
		String[] insertTheaterSQL = new String[10];
		String[] insertMemberSQL = new String[10];
		String[] insertScheduleSQL = new String[15];
		String[] insertReservationSQL = new String[10];
		String[] insertSeatSQL = new String[46];
		String[] insertTicketSQL = new String[10];
		
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
				+ "  `release_date` VARCHAR(45) NULL,\n"
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
				+ "  `round` INT NULL,\n"
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
		
		insertTheaterSQL[0] = "INSERT INTO Theater VALUES(1, 10, 'N');";
		insertTheaterSQL[1] = "INSERT INTO Theater VALUES(2, 5, 'N');";
		insertTheaterSQL[2] = "INSERT INTO Theater VALUES(3, 3, 'N');";
		insertTheaterSQL[3] = "INSERT INTO Theater VALUES(4, 5, 'N');";
		insertTheaterSQL[4] = "INSERT INTO Theater VALUES(5, 3, 'N');";
		insertTheaterSQL[5] = "INSERT INTO Theater VALUES(6, 5, 'N');";
		insertTheaterSQL[6] = "INSERT INTO Theater VALUES(7, 4, 'N');";
		insertTheaterSQL[7] = "INSERT INTO Theater VALUES(8, 4, 'N');";
		insertTheaterSQL[8] = "INSERT INTO Theater VALUES(9, 5, 'N');";
		insertTheaterSQL[9] = "INSERT INTO Theater VALUES(10, 2, 'N');";
		
		insertSeatSQL[0] = "INSERT INTO Seat VALUES(1, 'N', 1);";
		insertSeatSQL[1] = "INSERT INTO Seat VALUES(2, 'N', 1);";
		insertSeatSQL[2] = "INSERT INTO Seat VALUES(3, 'N', 1);";
		insertSeatSQL[3] = "INSERT INTO Seat VALUES(4, 'N', 1);";
		insertSeatSQL[4] = "INSERT INTO Seat VALUES(5, 'N', 1);";
		insertSeatSQL[5] = "INSERT INTO Seat VALUES(6, 'N', 1);";
		insertSeatSQL[6] = "INSERT INTO Seat VALUES(7, 'N', 1);";
		insertSeatSQL[7] = "INSERT INTO Seat VALUES(8, 'N', 1);";
		insertSeatSQL[8] = "INSERT INTO Seat VALUES(9, 'N', 1);";
		insertSeatSQL[9] = "INSERT INTO Seat VALUES(10, 'N', 1)";;
		insertSeatSQL[10] = "INSERT INTO Seat VALUES(11, 'N', 2);";
		insertSeatSQL[11] = "INSERT INTO Seat VALUES(12, 'N', 2);";
		insertSeatSQL[12] = "INSERT INTO Seat VALUES(13, 'N', 2);";
		insertSeatSQL[13] = "INSERT INTO Seat VALUES(14, 'N', 2);";
		insertSeatSQL[14] = "INSERT INTO Seat VALUES(15, 'N', 2);";
		insertSeatSQL[15] = "INSERT INTO Seat VALUES(16, 'N', 3);";
		insertSeatSQL[16] = "INSERT INTO Seat VALUES(17, 'N', 3);";
		insertSeatSQL[17] = "INSERT INTO Seat VALUES(18, 'N', 3);";
		insertSeatSQL[18] = "INSERT INTO Seat VALUES(19, 'N', 4);";
		insertSeatSQL[19] = "INSERT INTO Seat VALUES(20, 'N', 4);";
		insertSeatSQL[20] = "INSERT INTO Seat VALUES(21, 'N', 4);";
		insertSeatSQL[21] = "INSERT INTO Seat VALUES(22, 'N', 4);";
		insertSeatSQL[22] = "INSERT INTO Seat VALUES(23, 'N', 4);";
		insertSeatSQL[23] = "INSERT INTO Seat VALUES(24, 'N', 5);";
		insertSeatSQL[24] = "INSERT INTO Seat VALUES(25, 'N', 5);";
		insertSeatSQL[25] = "INSERT INTO Seat VALUES(26, 'N', 5);";
		insertSeatSQL[26] = "INSERT INTO Seat VALUES(27, 'N', 6);";
		insertSeatSQL[27] = "INSERT INTO Seat VALUES(28, 'N', 6);";
		insertSeatSQL[28] = "INSERT INTO Seat VALUES(29, 'N', 6);";
		insertSeatSQL[29] = "INSERT INTO Seat VALUES(30, 'N', 6);";
		insertSeatSQL[30] = "INSERT INTO Seat VALUES(31, 'N', 6);";
		insertSeatSQL[31] = "INSERT INTO Seat VALUES(32, 'N', 7);";
		insertSeatSQL[32] = "INSERT INTO Seat VALUES(33, 'N', 7);";
		insertSeatSQL[33] = "INSERT INTO Seat VALUES(34, 'N', 7);";
		insertSeatSQL[34] = "INSERT INTO Seat VALUES(35, 'N', 7);";
		insertSeatSQL[35] = "INSERT INTO Seat VALUES(36, 'N', 8);";
		insertSeatSQL[36] = "INSERT INTO Seat VALUES(37, 'N', 8);";
		insertSeatSQL[37] = "INSERT INTO Seat VALUES(38, 'N', 8);";
		insertSeatSQL[38] = "INSERT INTO Seat VALUES(39, 'N', 8);";
		insertSeatSQL[39] = "INSERT INTO Seat VALUES(40, 'N', 9);";
		insertSeatSQL[40] = "INSERT INTO Seat VALUES(41, 'N', 9);";
		insertSeatSQL[41] = "INSERT INTO Seat VALUES(42, 'N', 9);";
		insertSeatSQL[42] = "INSERT INTO Seat VALUES(43, 'N', 9);";
		insertSeatSQL[43] = "INSERT INTO Seat VALUES(44, 'N', 9);";
		insertSeatSQL[44] = "INSERT INTO Seat VALUES(45, 'N', 10);";
		insertSeatSQL[45] = "INSERT INTO Seat VALUES(46, 'N', 10);";
		
		
		
		insertMemberSQL[0] = "INSERT INTO Member VALUES(1, '김부각', '010-1234-5678', 'cweed@naver.com');";
		insertMemberSQL[1] = "INSERT INTO Member VALUES(2, '오징어집', '010-2222-3333', 'nongshim@gmail.com');";
		insertMemberSQL[2] = "INSERT INTO Member VALUES(3, '오감자', '010-3333-4555', 'potato@naver.com');";
		insertMemberSQL[3] = "INSERT INTO Member VALUES(4, '빼빼로', '010-1111-1111', 'pocky@naver.com');";
		insertMemberSQL[4] = "INSERT INTO Member VALUES(5, '양파링', '010-5555-6777', 'onion@naver.com');";
		insertMemberSQL[5] = "INSERT INTO Member VALUES(6, '새우깡', '010-1111-1111', 'shrimp@gmail.com');";
		insertMemberSQL[6] = "INSERT INTO Member VALUES(7, '고구마깡', '010-1111-1111', 'sweetpotato@gmail.com');";
		insertMemberSQL[7] = "INSERT INTO Member VALUES(8, '스윙칩', '010-1111-1111', 'swing@naver.com');";
		insertMemberSQL[8] = "INSERT INTO Member VALUES(9, '썬칩', '010-1111-1111', 'sun@naver.com');";
		insertMemberSQL[9] = "INSERT INTO Member VALUES(10, '꼬북칩', '010-1111-1111', 'turtle@naver.com');";
		
		insertScheduleSQL[0] = "INSERT INTO Schedule VALUES(1, '2021.02.03', '수', 1, '18:00', 1, 3);";
		insertScheduleSQL[1] = "INSERT INTO Schedule VALUES(2, '2021.05.04', '화', 1, '15:00', 2, 2);";
		insertScheduleSQL[2] = "INSERT INTO Schedule VALUES(3, '2021.05.05', '수', 1, '13:00', 3, 1);";
		insertScheduleSQL[3] = "INSERT INTO Schedule VALUES(4, '2021.05.06', '목', 1, '14:00', 4, 5);";
		insertScheduleSQL[4] = "INSERT INTO Schedule VALUES(5, '2021.05.07', '금', 1, '16:00', 5, 2);";
		insertScheduleSQL[5] = "INSERT INTO Schedule VALUES(6, '2021.05.08', '토', 1, '18:00', 6, 4);";
		insertScheduleSQL[6] = "INSERT INTO Schedule VALUES(7, '2021.05.09', '일', 1, '12:30', 7, 1);";
		insertScheduleSQL[7] = "INSERT INTO Schedule VALUES(8, '2021.05.10', '월', 1, '13:50', 8, 3);";
		insertScheduleSQL[8] = "INSERT INTO Schedule VALUES(9, '2021.05.11', '화', 1, '20:00', 9, 1);";
		insertScheduleSQL[9] = "INSERT INTO Schedule VALUES(10, '2021.05.12', '수', 1, '11:30', 10, 2);";
		insertScheduleSQL[10] = "INSERT INTO Schedule VALUES(11, '2021.05.13', '목', 1, '13:45', 11, 5);";
		insertScheduleSQL[11] = "INSERT INTO Schedule VALUES(12, '2021.05.15', '토', 1, '15:00', 12, 4);";
		insertScheduleSQL[12] = "INSERT INTO Schedule VALUES(13, '2021.05.16', '일', 2, '15:00', 12, 3);";
		insertScheduleSQL[13] = "INSERT INTO Schedule VALUES(14, '2021.05.17', '월', 3, '15:00', 12, 5);";
		insertScheduleSQL[14] = "INSERT INTO Schedule VALUES(15, '2021.05.18', '화', 4, '15:00', 12, 1);";
		
		insertReservationSQL[0] = "insert into reservation VALUES(1, '카드', '결제완료', '8000', '2021.05.05', 1);";
		insertReservationSQL[1] = "insert into reservation VALUES(2, '현금', '결제완료', '8000', '2021.06.06', 3);";
		insertReservationSQL[2] = "insert into reservation VALUES(3, '카드', '결제중', '16000', '2021.07.07', 3);";
		insertReservationSQL[3] = "insert into reservation VALUES(4, '카드', '결제오류', '24000', '2021.08.08', 5);";
		insertReservationSQL[4] = "insert into reservation VALUES(5, '현금', '결제완료', '8000', '2021.08.08', 5);";
		insertReservationSQL[5] = "insert into reservation VALUES(6, '카카오페이', '결제완료', '8000', '2021.08.08', 5);";
		insertReservationSQL[6] = "insert into reservation VALUES(7, '네이버페이', '결제완료', '8000', '2021.08.08', 4);";
		insertReservationSQL[7] = "insert into reservation VALUES(8, '카드', '결제완료', '8000', '2021.08.08', 3);";
		insertReservationSQL[8] = "insert into reservation VALUES(9, '현금', '결제완료', '8000', '2021.11.08', 5);";
		insertReservationSQL[9] = "insert into reservation VALUES(10, '카카오페이', '결제오류', '8000', '2021.08.08', 6);";
		
		insertTicketSQL[0] = "insert into ticket  values(1, '발권 완료', '8000', '8000', 3, 1, 16, 1);";
		insertTicketSQL[1] = "insert into ticket values(2, '미발권', '8000', '8000', 2, 2, 11, 2);";
		insertTicketSQL[2] = "insert into ticket values(3, '발권 완료', '16000', '16000', 1, 3, 3, 3);";
		insertTicketSQL[3] = "insert into ticket values(4, '미발권', '24000', '24000', 5, 4, 24, 4);";
		insertTicketSQL[4] = "insert into ticket values(5, '발권 완료', '8000', '8000', 2, 5, 12, 5);";
		insertTicketSQL[5] = "insert into ticket values(6, '발권 완료', '8000', '8000',2 ,5 ,13 ,10);";
		insertTicketSQL[6] = "insert into ticket values(7, '미발권', '8000', '8000',1 ,4 ,4 ,9);";
		insertTicketSQL[7] = "insert into ticket values(8, '미발권', '8000', '8000',1 ,9 ,5 ,8);";
		insertTicketSQL[8] = "insert into ticket values(9, '미발권', '8000', '8000',1 ,7 ,6 ,7);";
		insertTicketSQL[9] = "insert into ticket values(10, '발권 완료', '8000', '8000',1 ,9 ,7 ,6);";
		
		
		
		//설정한 string을 실행함
		executeSQL(initSQL);
		executeSQL(createSQL);
		executeSQL(insertMovieSQL);
		executeSQL(insertTheaterSQL);
		executeSQL(insertSeatSQL);
		executeSQL(insertMemberSQL);
		executeSQL(insertScheduleSQL);
		executeSQL(insertReservationSQL);
		executeSQL(insertTicketSQL);
		
		//initSeats();
		
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
		String[] expression = {" movie_name LIKE ", " director LIKE ", " actor LIKE ", " genre LIKE"};  
		String inputText = JOptionPane.showInputDialog("검색 조건을 영화명, 감독명, 배우명, 장르 순으로 입력해 주세요"
				+ "\nex)'봉준호'의 영화 조회"
				+ "\n없음 봉준호 없음 없음");
		String[] condition = inputText.split(" ");
		if (condition.length != 4) {
			JOptionPane.showMessageDialog(null, "양식에 맞춰 입력해 주세요!", "오류 메시지", JOptionPane.WARNING_MESSAGE);
			return;
		}
		// 사용자의 입력 조건에 따라 sql문 생성
		for (int i = 0; i < 4; ++i) {
			if (condition[i].equals("없음")) continue;
			sql += expression[i] + "'%" + condition[i] + "%'" + " and";
		}
		
		if (sql.charAt(sql.length() - 1) == 'd') {
			sql = sql.substring(0, sql.length() - 4);
		}
		else {
			sql = "SELECT * FROM Movie";
		}
		sql += ";";
		System.out.println(sql);
		
		//movie_id를 초기화
		movie_id = -1;
		row = -1;
		JFrame movieJf = new JFrame("영화 조회");
		JPanel mini = new JPanel();
		JLabel label = new JLabel("검색한 조건에 부합하는 영화 조회");
				
		//영화 리스트를 테이블로 불러오기
		movieTable = getTable("Movie", sql);
		movieTable.addMouseListener(this);
		
		JScrollPane tableData = new JScrollPane(movieTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tableData.setPreferredSize(new Dimension(500, 200));
		
		mini.setLayout(new BoxLayout(mini, BoxLayout.Y_AXIS));
		mini.add(label);
		mini.add(tableData);
		mini.add(btnMovieReservation);

		movieJf.setLocation(400, 300);
		movieJf.setSize(700, 450);
		movieJf.add(mini);
		movieJf.setVisible(true);
	}
	
	// searchMovie에서 조회한 영화에 대한 예매 기능
	public void movieReservation() {
		if (row == -1) {
			JOptionPane.showMessageDialog(null, "영화를 선택해 주세요!", "오류 메시지", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		JPanel reservPanel = new JPanel();
		JPanel confirmPanel = new JPanel();
		JLabel confirmLabel = new JLabel();
		String query = "", selectedTime = "";
		String[] method = {"신용카드", "휴대폰", "카카오페이", "네이버페이"};
		String[] ticketCount = {"1", "2", "3", "4"};
		ArrayList<String> schedule_time = new ArrayList<String> ();
		ArrayList<Integer> schedule_id = new ArrayList<Integer>();
		ArrayList<Integer> theater_id = new ArrayList<Integer>();
		int seat_num, selectedSchedule, result, selectedTheater, seat_id;
		String pay_method = "";
		
		//결제일자 확인을 위한 오늘 날짜
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
		String todayDate = today.format(formatter);
		
		//선택한 영화에 대한 상영 일정을 바탕으로 콤보박스 생성
		query = "SELECT * FROM Schedule WHERE movie_id = ";
		query += Integer.toString(movie_id) + ";";
		
		try {
	  	  	 Statement stmt = con.createStatement();
	  	  	 ResultSet rs = stmt.executeQuery(query);
	  	  	 while(rs.next()) {
	  	  		 schedule_id.add(rs.getInt(1));
	  	  		 schedule_time.add(rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(5));	  	 
	  	  		 theater_id.add(rs.getInt(7));
	  	  	 } 
	  	  } catch(SQLException e) {
	  	  	   e.printStackTrace();
	  	  	   return;
	  	    }
				
		// 관람 시각(=상영관) 과 결제수단 선택
		JComboBox scheduleCombo = new JComboBox(schedule_time.toArray(new String[schedule_time.size()]));
		JComboBox methodCombo = new JComboBox(method);
		JComboBox ticketCombo = new JComboBox(ticketCount);
				
		reservPanel.add(new JLabel("관람 시각을 선택하세요: "));
		reservPanel.add(scheduleCombo);
		reservPanel.add(Box.createHorizontalStrut(10));
		reservPanel.add(new JLabel("결제 수단을 선택해 주세요: "));
		reservPanel.add(methodCombo);
		reservPanel.add(new JLabel("예매하실 티켓의 수를 선택해 주세요: "));
		reservPanel.add(ticketCombo);
		result = JOptionPane.showConfirmDialog(null, reservPanel, "날짜 및 결제방법을 선택해 주세요.", JOptionPane.OK_CANCEL_OPTION);
		
		// 사용자가 선택한 날짜를 기반으로 schedule_id 받아옴
		selectedTime = scheduleCombo.getSelectedItem().toString();
		int idx = schedule_time.indexOf(selectedTime);
		selectedSchedule = schedule_id.get(idx);
		pay_method = methodCombo.getSelectedItem().toString();
		int ticket_count = Integer.parseInt(ticketCombo.getSelectedItem().toString());
		int pay_amount = ticket_count * ticket_price;
		
		// 상영관 등을 보여주며 최종 확인
		selectedTheater = theater_id.get(idx);
		confirmPanel.add(new JLabel("상영 시간 : "));
		confirmPanel.add(new JLabel(selectedTime));
		reservPanel.add(Box.createHorizontalStrut(10));
		confirmPanel.add(new JLabel(", 상영관 : "));
		confirmPanel.add(new JLabel(Integer.toString(selectedTheater)));
		
		result = JOptionPane.showConfirmDialog(null, confirmPanel, "선택하신 일정과 상영관을 확인해 주세요. 예매하시겠습니까?", JOptionPane.OK_CANCEL_OPTION);
		
		if (result == JOptionPane.YES_OPTION) {
			
			int reservation_id = 0, ticket_id = 0;
			
			//마지막 튜플의 reservation_id의 다음 숫자를 id로 지정
			reservation_id = getPK("reservation");
			
			String sql[] = {"INSERT INTO Reservation VALUES("};
			sql[0] += Integer.toString(reservation_id) + ", '" + pay_method + "', '결제 완료', '" + Integer.toString(pay_amount) + "', '" + todayDate + "', " + User_id + ");";
			executeSQL(sql);
			
			//여기부터 변경사항
			
			// 티켓 숫자만큼 '티켓' 생성
			for (int i = 0; i < ticket_count; ++i) {
				//마지막 튜플의 ticket_id의 다음 숫자를 id로 지정
				ticket_id = getPK("ticket");
				
				//사용자가 선택한 상영관의 빈 좌석 중 첫 번재 좌석 가져오기
				query = "SELECT seat_id FROM Seat WHERE seat_use = 'N' and theater_id = ";
				query += Integer.toString(selectedTheater) + ";";
				try {
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery(query);
					rs.next();
					seat_id = rs.getInt(1);
				} catch(SQLException e) {
					System.out.println(e.getMessage());
					return;
				}
				
				//사용한 좌석번호 변경
				System.out.println(seat_id);
				sql[0] = "UPDATE Seat SET seat_use = 'Y' WHERE seat_id =";
				sql[0] += Integer.toString(seat_id) + ";";
				executeSQL(sql);
				
				// 예매에 따른 티켓정보 자동 생성
				sql[0] = "INSERT INTO Ticket VALUES(";
				sql[0] += Integer.toString(ticket_id) + ", '발권하지 않음', '" + Integer.toString(ticket_price) + "', '" + Integer.toString(ticket_price) + "', "
						+ Integer.toString(selectedTheater) + ", " + Integer.toString(selectedSchedule) + ", " + Integer.toString(seat_id) + ", " + Integer.toString(reservation_id) + ");";
				System.out.println(sql[0]);
				executeSQL(sql);
			}
			
		}
		else {
			JOptionPane.showMessageDialog(null, "예매가 취소되었습니다.", "취소", JOptionPane.ERROR_MESSAGE);
		}
	}

	// 사용하지 않은 primary key를 자동 반환
	int getPK(String table) {
		
		String id = table + "_id";
		int last_id = 0;
		//마지막 튜플의 PK의 다음 숫자를 id로 지정
		try {
			Statement stmt = con.createStatement();
			String query = "SELECT " + id + " FROM " + table + " ORDER BY " + id + ";"; 
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				last_id = rs.getInt(1);
			}
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return -1;
		}
		return last_id + 1;		
	}
	
	private void showUserReservation() {
		JFrame reservastionJf = new JFrame("예매 현황 출력");
		JPanel mini = new JPanel();
		JPanel bottomPanel = new JPanel();
		
		row = -1;
		// 예매 현황을 테이블로 불러오기
		reservationTable = getUserReservationTable();
		reservationTable.addMouseListener(this);
		
		//getTable 메소드를 이용해 테이블을 받아오기
		JScrollPane tableData = new JScrollPane(reservationTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tableData.setPreferredSize(new Dimension(500, 200));
		
		mini.setLayout(new BoxLayout(mini, BoxLayout.Y_AXIS));
		mini.add(tableData);
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		bottomPanel.add(delete_reserv_btn);
		bottomPanel.add(update_movie_btn);
		bottomPanel.add(update_schedule_btn);
		mini.add(bottomPanel);

		reservastionJf.setLocation(400, 300);
		reservastionJf.setSize(700, 450);
		reservastionJf.add(mini);
		reservastionJf.setVisible(true);
		
		
	}
	
	private JTable getUserReservationTable() {
		
		String query = "select Ti.ticket_id, Mo.movie_name, Sc.date, Ti.theater_id, Ti.seat_id, Ti.price from reservation as Re, ticket as Ti, schedule as Sc, Movie as Mo"
				+ " where Re.reservation_id = Ti.reservation_id and Ti.schedule_id = Sc.schedule_id and Sc.movie_id = Mo.movie_id";
		query += " and Re.member_id = " + Integer.toString(User_id);
		
		String countQuery = "SELECT COUNT(*) from (" + query + ") as WT;";
		query += ";";
		System.out.println(query);
		System.out.println(countQuery);
				
		int i = 0;
		
		//속성명 받아오기
		String columnName[] = {"티켓번호", "영화명", "상영일", "상영관번호", "좌석번호", "판매가격"};
		
		//위에서 결정한 속성 개수(attribute)와 header에 따라 테이블을 출력함
		try {
			//stmt 및 data 설정
			Statement stmt1 = con.createStatement();
			Statement stmt2 = con.createStatement();
			
			//튜플의 개수 가져오기
			ResultSet countRS = stmt1.executeQuery(countQuery);
			countRS.next();
			int count = countRS.getInt(1);

			
			if (count == 0) {
				JOptionPane.showMessageDialog(null, "나의 예매 내역이 존재하지 않습니다!", "오류 메시지", JOptionPane.WARNING_MESSAGE);
				return null;
			
			}

			ResultSet rs = stmt2.executeQuery(query);


			//데이터베이스 크기만큼의 2차원 배열 선언
			String data[][] = new String[count][columnName.length];

			//2차원 배열 data에 table 순회하며 데이터 저장
			i = 0;
			while(rs.next()) {
				for (int j = 0; j < columnName.length; ++j) {
					if (j == 3 || j == 4) 
						data[i][j] = Integer.toString(rs.getInt(j+1));
					else
						data[i][j] = rs.getString(j+1);
				}
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
	
	private void deleteReserve() {
		if (row == -1) {
			JOptionPane.showMessageDialog(null, "영화를 선택해 주세요!", "오류 메시지", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// 삭제 시 티켓id를 기반으로 ticket 튜플 삭제, seat_id를 기반으로 seat를 다시 사용가능한 상태로 변경
		String[] query = {"DELETE FROM Ticket WHERE ticket_id = ", "UPDATE Seat SET seat_use = 'N' WHERE seat_id = "};
		String seatQuery;
		int seat_id = -1, ticket_id = -1;

		int result = JOptionPane.showConfirmDialog(null, "정말 해당 티켓을 삭제하시겠습니까?", "선택하신 일정과 상영관을 확인해 주세요. 예매하시겠습니까?", JOptionPane.OK_CANCEL_OPTION);
		
		if (result == JOptionPane.YES_OPTION) {
			// 다중 선택
			if (rows.length > 1) {
				
				String[] sql = new String[selected_IDs.size()];
				String[] sql2 = new String[selected_IDs.size()];
				for (int i = 0; i < sql.length; ++i) {

					ticket_id = selected_IDs.get(i);
					seatQuery = "SELECT seat_id FROM Ticket WHERE ticket_id = ";
					seatQuery += Integer.toString(ticket_id) + ";";
					try {
						stmt = con.createStatement();
						rs = stmt.executeQuery(seatQuery);
						rs.next();
						seat_id = rs.getInt(1);
					} catch(SQLException e){
						e.printStackTrace();
						return;
					}
					
					sql[i] = query[0] + Integer.toString(ticket_id) + ";";
					sql2[i] = query[1] + Integer.toString(seat_id) + ";";

				}
				executeSQL(sql);
				executeSQL(sql2);
			}
			// 단일 선택
			else {
				seatQuery = "SELECT seat_id FROM Ticket WHERE ticket_id = ";
				seatQuery += Integer.toString(movie_id) + ";";
				try {
					stmt = con.createStatement();
					rs = stmt.executeQuery(seatQuery);
					rs.next();
					seat_id = rs.getInt(1);
				} catch(SQLException e){
					e.printStackTrace();
					return;
				}
				query[1] += Integer.toString(seat_id) + ";";
				query[0] += Integer.toString(movie_id) + ";";
				executeSQL(query);
			}
			
			// 만약 해당 예약에 대해 모든 티켓이 삭제되면, 해당 예약 번호도 삭제?
			
		}
		else {
			JOptionPane.showMessageDialog(null, "취소되었습니다.", "알림", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	// movie_id와 ticket_id를 받아 해당 티켓에 새로운 상영일정 부여
	private void newSchedule(int movieId, int ticketId) {
		// 사용자에게 새로운 상영 일정 선택하게 하기
		
		String query;
		
		JPanel updatePanel = new JPanel();
		JPanel confirmPanel = new JPanel();
		ArrayList<String> schedule_time = new ArrayList<String> ();
		ArrayList<Integer> schedule_id = new ArrayList<Integer>();
		ArrayList<Integer> theater_id = new ArrayList<Integer>();
		int selectedSchedule, result, selectedTheater;
		String selectedTime, issue, stdPrice, price;
		int reservationId, seatId;
		
		//선택한 영화에 대한 상영 일정을 바탕으로 콤보박스 생성
		query = "SELECT * FROM Schedule WHERE movie_id = ";
		query += Integer.toString(movieId) + ";";
		
		try {
	  	  	 Statement stmt = con.createStatement();
	  	  	 ResultSet rs = stmt.executeQuery(query);
	  	  	 while(rs.next()) {
	  	  		 schedule_id.add(rs.getInt(1));
	  	  		 schedule_time.add(rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(5));	  	 
	  	  		 theater_id.add(rs.getInt(7));
	  	  	 } 
	  	  } catch(SQLException e) {
	  	  	   e.printStackTrace();
	  	  	   return;
	  	    }
				
		// 관람 시각(=상영관) 선택
		JComboBox scheduleCombo = new JComboBox(schedule_time.toArray(new String[schedule_time.size()]));
				
		updatePanel.add(new JLabel("관람 시각을 선택하세요: "));
		updatePanel.add(scheduleCombo);
		result = JOptionPane.showConfirmDialog(null, updatePanel, "변경할 상영일을 선택해 주세요.", JOptionPane.OK_CANCEL_OPTION);
		
		// 사용자가 선택한 날짜를 기반으로 schedule_id 받아옴
		selectedTime = scheduleCombo.getSelectedItem().toString();
		int idx = schedule_time.indexOf(selectedTime);
		selectedSchedule = schedule_id.get(idx);
		
		// 상영관 등을 보여주며 최종 확인
		selectedTheater = theater_id.get(idx);
		confirmPanel.add(new JLabel("상영 시간 : "));
		confirmPanel.add(new JLabel(selectedTime));
		confirmPanel.add(Box.createHorizontalStrut(10));
		confirmPanel.add(new JLabel(", 상영관 : "));
		confirmPanel.add(new JLabel(Integer.toString(selectedTheater)));
		
		result = JOptionPane.showConfirmDialog(null, confirmPanel, "선택하신 일정과 상영관을 확인해 주세요. 진행하시겠습니까?", JOptionPane.OK_CANCEL_OPTION);
			
		if (result == JOptionPane.YES_OPTION) {
			
			// 삭제 전, 기존 티켓의 값들 가져오기
			query = "SELECT * FROM Ticket WHERE ticket_id = ";
			query += Integer.toString(ticketId) + ";";
			try {
				stmt = con.createStatement();
				rs = stmt.executeQuery(query);
				rs.next();
				issue = rs.getString(2);
				stdPrice = rs.getString(3);
				price = rs.getString(4);
				seatId = rs.getInt(7);
				reservationId = rs.getInt(8);
			} catch (SQLException e) {
				e.printStackTrace();
				return;
			}
			// 해당 티켓 삭제			
			String sql[] = {"DELETE FROM Ticket WHERE ticket_id ="};
			sql[0] += Integer.toString(ticketId) + ";";
			
			System.out.println(sql[0]);
			executeSQL(sql);		
			
			// 티켓 삭제 후 사용 여부 N으로 변환
			String[] seatQuery = {"UPDATE Seat SET seat_use = 'N' WHERE seat_id = "};
			seatQuery[0] += Integer.toString(seatId) + ";";
			executeSQL(seatQuery);
			
			
			//사용자가 선택한 상영관의 빈 좌석 중 첫 번째 좌석 가져오기
			query = "SELECT seat_id FROM Seat WHERE seat_use = 'N' and theater_id = ";
			query += Integer.toString(selectedTheater) + ";";
			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				rs.next();
				seatId = rs.getInt(1);
			} catch(SQLException e) {
				System.out.println(e.getMessage());
				return;
			}
				
			//사용한 좌석번호 변경
			sql[0] = "UPDATE Seat SET seat_use = 'Y' WHERE seat_id =";
			sql[0] += Integer.toString(seatId) + ";";
			executeSQL(sql);
			
			// 예매에 따른 티켓정보 자동 생성
			sql[0] = "INSERT INTO Ticket VALUES(";
			sql[0] += Integer.toString(ticketId) + ", '발권하지 않음', '" + stdPrice + "', '" + price + "', " + Integer.toString(selectedTheater) 
			+ ", " + Integer.toString(selectedSchedule) + ", " + Integer.toString(seatId) + ", " + Integer.toString(reservationId) + ");";
			System.out.println(sql[0]);
			executeSQL(sql);
		}
		else {
			JOptionPane.showMessageDialog(null, "변경이 취소되었습니다.", "취소", JOptionPane.ERROR_MESSAGE);
		}
	}

	// 사용자 - 상영일정 변경
	private void updateSchedule() {
		if (row == -1) {
			JOptionPane.showMessageDialog(null, "영화를 선택해 주세요!", "오류 메시지", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		int ticketId = movie_id, movieId;
		String query = "";
		
		// 사용자가 선택한 티켓을 기반으로 movie_id 가져오기
		query = "select movie_id from schedule where schedule_id = (select schedule_id from ticket where ticket_id = ";
		query += Integer.toString(ticketId) + ");";

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			movieId = rs.getInt(1);
			
		} catch(SQLException e) {
			e.printStackTrace();
			return;
		}
		
		newSchedule(movieId, ticketId);
		
	}

	//사용자 - 영화 변경
	private void changeMovie() {
		if (row == -1) {
			JOptionPane.showMessageDialog(null, "영화를 선택해 주세요!", "오류 메시지", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		int ticketId = movie_id;
		movie_id = -1;
		row = -1;
		
		JFrame movieJf = new JFrame("영화 조회");
		JPanel mini = new JPanel();
		JLabel label = new JLabel("변경하고 싶은 영화를 선택하세요");
		JButton btnMovieSelect = new JButton("선택 완료");
				
		// 전체 영화 리스트 불러온 후 창으로 띄우기
		movieTable = getTable("Movie", "");
		movieTable.addMouseListener(this);
		
		JScrollPane tableData = new JScrollPane(movieTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tableData.setPreferredSize(new Dimension(500, 200));
		
		mini.setLayout(new BoxLayout(mini, BoxLayout.Y_AXIS));
		mini.add(label);
		mini.add(tableData);
		mini.add(btnMovieSelect);

		movieJf.setLocation(400, 300);
		movieJf.setSize(700, 450);
		movieJf.add(mini);
		
		btnMovieSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newSchedule(movie_id, ticketId);
			}
		});
		
		movieJf.setVisible(true);
		
	}
	
	
	public void mouseClicked(MouseEvent e) {
		JTable table = (JTable)e.getComponent();
		TableModel model = (TableModel)table.getModel();
		selected_IDs.clear();
		column = table.getSelectedColumn();
		row = table.getSelectedRow();
		rows = table.getSelectedRows();
		
		for (int i = 0; i < rows.length; ++i) {
			selected_IDs.add(Integer.parseInt((String) model.getValueAt(rows[i], 0)));
		}
		
		//각 테이블의 movie_id 혹은 reservation_id
		movie_id = Integer.parseInt((String) model.getValueAt(row, 0));
		
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
		jc19011051.setFrame();
	}


	
}


