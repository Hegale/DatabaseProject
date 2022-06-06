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
	
	
	//�������� ���ƿ��� ��ư
	JButton btnReturnMainFromAdmin, btnReturnMainFromProfessor, btnReturnMainFromStudent;
	//������ ������ adminPanel�� ��ư
	JButton btnResetDB, btnInsert, btnDelete, btnModify, btnViewAll;
	//ȸ�� ������ userPanel�� ��ư
	JButton btnSearchMovie, btnReservation, btnMovieReservation, delete_reserv_btn, update_movie_btn, update_schedule_btn;
 
	

	JScrollPane scrollPane; // txtResul�� �־��� JScrollPane
	JTextArea txtResult; // ������� ������ JTextArea (Center�� �� ����)
	
	JTable movieTable; // ���� ���������� ��ȭ ����� �����ִ� table
	JTable reservationTable; // ���� ���������� ��ȭ ����� �����ִ� table
	
	//JOptionpane �ʱ� ȸ��ID �Է�â 
	JOptionPane inputUserIdPane;
	
	// ���� ���� ���� ��¥�� ���Ƿ� 2021�� 5�� 5�Ϸ� ����
	String nowDate = "2021.05.05";
	int User_id = -1; //ȸ�� ���̵� ���� ����
	
	
	// ���̺��� �������� �� �ش� ��ġ�� ���� �޾ƿ��� ������
	int row = -1, column = -1, movie_id = -1;
	int ticket_price = 8000; //Ƽ�� ����
	
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
		
		delete_reserv_btn = new JButton("���� ���");
		update_movie_btn = new JButton("��ȭ ����");
		update_schedule_btn = new JButton("���� ����");
		
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
		reservationPanel.add(delete_reserv_btn); 
		reservationPanel.add(update_movie_btn); 
		reservationPanel.add(update_schedule_btn); 
		
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
		btnMovieReservation.addActionListener(this);
		
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
				
				User_id = Integer.parseInt(id);
				// id �߸� �Է½� ���� ǥ��!
					
				// member ���̺��� memberid ȸ�� ã�� ����
				String query = "SELECT * FROM member WHERE memberid = " + User_id;
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
			String tableName = JOptionPane.showInputDialog("�Է��� ���̺� �̸��� �Է��Ͻÿ�. "
					+ "(Member, Movie, Reservation, Schedule, Seat, Theater, Ticket)");
			
			// ���̺� �Է� �߸��Է��ϸ� ���� ��� 
			if (true) {
				
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

                    JOptionPane.showConfirmDialog(null, insertMemberPanel,"Member ���̺��� �Ӽ������� �Է��ϼ���.", JOptionPane.OK_CANCEL_OPTION);

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
                        JOptionPane.showMessageDialog(null, "SQL Exception\n"+e1.getMessage(), "����", JOptionPane.ERROR_MESSAGE);
                    }
                }	
			}catch(NumberFormatException e3) {
                JOptionPane.showMessageDialog(null, "�߸��� �Է��Դϴ�\n"+e3.getMessage(), "����", JOptionPane.ERROR_MESSAGE);
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

                    JOptionPane.showConfirmDialog(null, insertMoviePanel,"Movie ���̺��� �Ӽ������� �Է��ϼ���.", JOptionPane.OK_CANCEL_OPTION);

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

                        String insertSQL = "insert into Movie (movie_id, movie_name, screentime, rating, director, actor, genre, introduce, release_date)"
                                + " values("+movie_id_int+",'"+movie_name_str+"','"+screentime_str+"', '"+rating_str+"' , '"
                        		+rating_str+"', '"+director_str+"', '"+actor_str+"', '"+genre_str+"', '"+introduce_str+"', '"+release_str+"');";
                        stmt.executeUpdate(insertSQL);

                    }catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "SQL Exception\n"+e1.getMessage(), "����", JOptionPane.ERROR_MESSAGE);
                    }
                }	
			}catch(NumberFormatException e3) {
                JOptionPane.showMessageDialog(null, "�߸��� �Է��Դϴ�\n"+e3.getMessage(), "����", JOptionPane.ERROR_MESSAGE);
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

                    JOptionPane.showConfirmDialog(null, insertReservationPanel,"Reservation ���̺��� �Ӽ������� �Է��ϼ���.", JOptionPane.OK_CANCEL_OPTION);

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
                        JOptionPane.showMessageDialog(null, "SQL Exception\n"+e1.getMessage(), "����", JOptionPane.ERROR_MESSAGE);
                    }
                }	
			}catch(NumberFormatException e3) {
                JOptionPane.showMessageDialog(null, "�߸��� �Է��Դϴ�\n"+e3.getMessage(), "����", JOptionPane.ERROR_MESSAGE);
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

                    JOptionPane.showConfirmDialog(null, insertSchedulePanel,"Schedule ���̺��� �Ӽ������� �Է��ϼ���.", JOptionPane.OK_CANCEL_OPTION);

                    int schedule_id_int = Integer.parseInt(schedule_id.getText());
                    String date_str = (date.getText());
                    String day_str = (day.getText());
                    String round_str = (round.getText());
                    String time_str = (time.getText());
                    int movie_id_int = Integer.parseInt(movie_id.getText());
                    int theater_id_int = Integer.parseInt(theater_id.getText());
                    
//                    // date�� ������ ���̸� ó��������Ѵ�
//                    Date movie_date = null;
//                    Date schedule_date = null;
//                    
//                    // ������        
//                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");         
//                    // ���ڿ� -> Date        
//                    try {
//						schedule_date = formatter.parse(date_str);
//					} catch (ParseException e2) {
//						e2.printStackTrace();
//					}         
//                    
//                
//					try {
//						stmt = con.createStatement();
//						
//						String find_movie_date_SQL = "select release_date from movie where movie_id = " + movie_id_int + ";";
//						System.out.println(find_movie_date_SQL);
//						rs = stmt.executeQuery(find_movie_date_SQL);
//						try {
//							movie_date = formatter.parse(rs.getString(1));
//							System.out.print(movie_date);
//						} catch (ParseException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
//					} catch (SQLException e2) {
//						// TODO Auto-generated catch block
//						e2.printStackTrace();
//					}
//					
//					// movie_date > schedule_date -> campare > 0
//                    int compare = movie_date.compareTo(schedule_date);
//                    if (compare < 0) {
//                    	JOptionPane.showMessageDialog(null, "�ش� ��¥�� �� ������ ���� �� �����ϴ�.\n", "�ٸ� ���� �����ϼ���.", JOptionPane.ERROR_MESSAGE);
//                    	throw new Exception();
//                    }
                    
                     
                    
                    
                    try {
                        stmt = con.createStatement();

                        String insertSQL = "insert into Schedule (schedule_id, date, day, round, time, movie_id, theater_id)"
                                + " values("+schedule_id_int+",'"+date_str+"','"+day_str+"', '"+round_str+"' , '"
                        		+time_str+"', '"+movie_id_int+"', '"+theater_id_int+"');";
                        stmt.executeUpdate(insertSQL);

                    }catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "SQL Exception\n"+e1.getMessage(), "����", JOptionPane.ERROR_MESSAGE);
                    }
                }	
			}catch(NumberFormatException e3) {
                JOptionPane.showMessageDialog(null, "�߸��� �Է��Դϴ�\n"+e3.getMessage(), "����", JOptionPane.ERROR_MESSAGE);
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
                   
                    JOptionPane.showConfirmDialog(null, insertSeatPanel,"Seat ���̺��� �Ӽ������� �Է��ϼ���.", JOptionPane.OK_CANCEL_OPTION);

                    int seat_id_int = Integer.parseInt(seat_id.getText());
                    String seat_use_str = seat_use.getText();
                    int theater_id_int = Integer.parseInt(theater_id.getText());

                    try {

                        Statement stmt = con.createStatement();

                        String insertSQL = "insert into Seat (seat_id, seat_use, theater_id)"
                                + " values("+seat_id_int+",'"+seat_use_str+"','"+theater_id_int+"');";
                        stmt.executeUpdate(insertSQL);

                    }catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "SQL Exception\n"+e1.getMessage(), "����", JOptionPane.ERROR_MESSAGE);
                    }
                }	
			}catch(NumberFormatException e3) {
                JOptionPane.showMessageDialog(null, "�߸��� �Է��Դϴ�\n"+e3.getMessage(), "����", JOptionPane.ERROR_MESSAGE);
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
                    
                    int result = JOptionPane.showConfirmDialog(null, myPanel,"Movie ���̺��� �Ӽ������� �Է��ϼ���.", JOptionPane.OK_CANCEL_OPTION);

                    int theater_id_int = Integer.parseInt(theater_id.getText());
                    int seat_num_int = Integer.parseInt(seat_num.getText());
                    String theater_use_str = theater_use.getText();
                    
                    try {
                        Statement stmt = con.createStatement();

                        String insertSQL = "insert into Theater (theater_id, seat_num, theater_use)"
                                + " values("+theater_id_int+",'"+seat_num_int+"','"+theater_use_str+"');";
                        stmt.executeUpdate(insertSQL);

                    }catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "SQL Exception\n"+e1.getMessage(), "����", JOptionPane.ERROR_MESSAGE);
                    }
                }	
			}catch(NumberFormatException e3) {
                JOptionPane.showMessageDialog(null, "�߸��� �Է��Դϴ�\n"+e3.getMessage(), "����", JOptionPane.ERROR_MESSAGE);
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

                    JOptionPane.showConfirmDialog(null, myPanel,"Ticket ���̺��� �Ӽ������� �Է��ϼ���.", JOptionPane.OK_CANCEL_OPTION);

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
                        JOptionPane.showMessageDialog(null, "SQL Exception\n"+e1.getMessage(), "����", JOptionPane.ERROR_MESSAGE);
                    }
                }	
			}catch(NumberFormatException e3) {
                JOptionPane.showMessageDialog(null, "�߸��� �Է��Դϴ�\n"+e3.getMessage(), "����", JOptionPane.ERROR_MESSAGE);
                return;
            }
			
		}
		else if(e.getSource() == btnDelete) {
			
			JTextField tableName = new JTextField(8);
            JTextField where = new JTextField(8);
            JPanel deletePanel = new JPanel();

            deletePanel.add(new JLabel("� ���̺��� �����Ͻðڽ��ϱ�?"));
            deletePanel.add(tableName);
            deletePanel.add(Box.createHorizontalStrut(2));
            deletePanel.add(new JLabel("���ǽ��� ���ּ���"));
            deletePanel.add(where);

            try {
                JOptionPane.showConfirmDialog(null, deletePanel,"�Է�!!!", JOptionPane.OK_CANCEL_OPTION);


                String tableName_str = tableName.getText();
                String where_str = where.getText();
                Statement stmt = con.createStatement();
                String deleteSQL = "Delete from " + tableName_str + " where " + where_str + ";";

                stmt.execute("set sql_safe_updates = 0;");
                stmt.executeUpdate(deleteSQL);

            }
            catch (SQLException e1) {
                JOptionPane.showMessageDialog(null, "�߸��� �Է��Դϴ�.\n"+e1.getMessage(), "�Է� ����", JOptionPane.ERROR_MESSAGE);
            }
            catch (Exception e2) {
            }
			
		}
		else if(e.getSource() == btnModify) {
			JTextField tableName = new JTextField(8);
            JTextField set = new JTextField(8);
            JTextField where = new JTextField(8);
            JPanel modifyPanel = new JPanel();

            modifyPanel.add(new JLabel("�Ӽ� ������ ���̺�:"));
            modifyPanel.add(tableName);
            modifyPanel.add(Box.createHorizontalStrut(2));
            modifyPanel.add(new JLabel("�����ҳ��� ex) member_name = '�Ѹ��Ѹ�' :"));
            modifyPanel.add(set);
            modifyPanel.add(Box.createHorizontalStrut(2));
            modifyPanel.add(new JLabel("���ǽ� ex) member_id = 1 :"));
            modifyPanel.add(where);

            JOptionPane.showConfirmDialog(null, modifyPanel,"�Է��Ͻÿ�", JOptionPane.OK_CANCEL_OPTION);

            String tableName_str = (tableName.getText());
            String set_str = (set.getText());
            String where_str = (where.getText());


            try {
                Statement stmt = con.createStatement();
                String updateSQL = "Update " + tableName_str + " Set " + set_str + " where " + where_str + ";";

                stmt.executeUpdate(updateSQL);

            }catch (SQLException e1) {
                JOptionPane.showMessageDialog(null, "�Է� �����Դϴ�.\n"+e1.getMessage(), "����", JOptionPane.ERROR_MESSAGE);
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
			
		}
		else if (e.getSource() == update_movie_btn) {
			
		}
		else if (e.getSource() == update_schedule_btn) {
	
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
		String[] insertTheaterSQL = new String[5];
		String[] insertMemberSQL = new String[5];
		String[] insertSchedule = new String[15];
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
		
		insertTheaterSQL[0] = "INSERT INTO Theater VALUES(1, 10, 'N');";
		insertTheaterSQL[1] = "INSERT INTO Theater VALUES(2, 5, 'N');";
		insertTheaterSQL[2] = "INSERT INTO Theater VALUES(3, 3, 'N');";
		insertTheaterSQL[3] = "INSERT INTO Theater VALUES(4, 5, 'N');";
		insertTheaterSQL[4] = "INSERT INTO Theater VALUES(5, 10, 'N');";
		
		insertMemberSQL[0] = "INSERT INTO Member VALUES(1, '��ΰ�', '010-1234-5678', 'cweed@naver.com');";
		insertMemberSQL[1] = "INSERT INTO Member VALUES(2, '��¡����', '010-2222-3333', 'nongshim@gmail.com');";
		insertMemberSQL[2] = "INSERT INTO Member VALUES(3, '������', '010-3333-4555', 'potato@naver.com');";
		insertMemberSQL[3] = "INSERT INTO Member VALUES(4, '������', '010-1111-1111', 'pocky@naver.com');";
		insertMemberSQL[4] = "INSERT INTO Member VALUES(5, '���ĸ�', '010-5555-6777', 'onion@naver.com');";
		
		insertSchedule[0] = "INSERT INTO Schedule VALUES(1, '2022.02.03', '��', 1, '18:00', 1, 3);";
		insertSchedule[1] = "INSERT INTO Schedule VALUES(2, '2022.05.04', '��', 1, '15:00', 2, 2);";
		insertSchedule[2] = "INSERT INTO Schedule VALUES(3, '2022.05.05', '��', 1, '13:00', 3, 1);";
		insertSchedule[3] = "INSERT INTO Schedule VALUES(4, '2022.05.06', '��', 1, '14:00', 4, 5);";
		insertSchedule[4] = "INSERT INTO Schedule VALUES(5, '2022.05.07', '��', 1, '16:00', 5, 2);";
		insertSchedule[5] = "INSERT INTO Schedule VALUES(6, '2022.05.08', '��', 1, '18:00', 6, 4);";
		insertSchedule[6] = "INSERT INTO Schedule VALUES(7, '2022.05.09', '��', 1, '12:30', 7, 1);";
		insertSchedule[7] = "INSERT INTO Schedule VALUES(8, '2022.05.10', 'ȭ', 1, '13:50', 8, 3);";
		insertSchedule[8] = "INSERT INTO Schedule VALUES(9, '2022.05.11', '��', 1, '20:00', 9, 1);";
		insertSchedule[9] = "INSERT INTO Schedule VALUES(10, '2022.05.12', '��', 1, '11:30', 10, 2);";
		insertSchedule[10] = "INSERT INTO Schedule VALUES(11, '2022.05.13', '��', 1, '13:45', 11, 5);";
		insertSchedule[11] = "INSERT INTO Schedule VALUES(12, '2022.05.15', '��', 1, '15:00', 12, 4);";
		insertSchedule[12] = "INSERT INTO Schedule VALUES(13, '2022.05.15', '��', 2, '15:00', 12, 3);";
		insertSchedule[13] = "INSERT INTO Schedule VALUES(14, '2022.05.15', '��', 3, '15:00', 12, 5);";
		insertSchedule[14] = "INSERT INTO Schedule VALUES(15, '2022.05.15', '��', 4, '15:00', 12, 1);";
		
		//������ string�� ������
		executeSQL(initSQL);
		executeSQL(createSQL);
		executeSQL(insertMovieSQL);
		executeSQL(insertTheaterSQL);
		executeSQL(insertMemberSQL);
		executeSQL(insertSchedule);
		
		initSeats();
		
		//executeSQL(insertSQL, insertSQL.length);
		JOptionPane.showMessageDialog(null, "�ʱ�ȭ �Ϸ�", "�˸�", JOptionPane.DEFAULT_OPTION);
	}
	
	// �� �󿵰��� ���� seat_num�� ������� Seat�� Ʃ���� �ڵ�����
	private void initSeats() {
		
		String query="SELECT * FROM Theater;"; /* SQL �� */
		int theater_id, seat_num;
	  	  try { /* �����ͺ��̽��� ���� ����� �������� ���� */
	  	  	 Statement stmt = con.createStatement();
	  	  	 ResultSet rs = stmt.executeQuery(query);
	  	  	 while(rs.next()) {
	  	  		 
	  	  	 	theater_id = rs.getInt(1);
	  	  	 	seat_num = rs.getInt(2);
	  	  	 	buildSeats(theater_id, seat_num);
	  	  	 	
	  	  	 }
	  	  	 
	  	  } catch(SQLException e) {
	  	  	   e.printStackTrace();
	  	    }
		
	}

	//���ڷ� ���� ��ȭ���� ���� seat_num ��ŭ�� Ʃ���� Seat ���̺� Insert
	private void buildSeats(int theater_id, int seat_num) {
		
		String query = "SELECT count(*) FROM Seat;";
		String[] sql = new String[seat_num];
		int num;
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			num = rs.getInt(1);
		} catch(SQLException e) {
			e.printStackTrace();
			return;
		}

		query = "INSERT INTO Seat VALUES(";
		num += 1;
		
		for (int i = num; i < num + seat_num; ++i) {
			sql[i-num] = query + Integer.toString(i) + ", 'N', " + Integer.toString(theater_id) + ");";
		}
		executeSQL(sql);
		
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
		String[] expression = {" movie_name LIKE ", " director LIKE ", " actor LIKE ", " genre LIKE"};  
		String inputText = JOptionPane.showInputDialog("�˻� ������ ��ȭ��, ������, ����, �帣 ������ �Է��� �ּ���"
				+ "\nex)'����ȣ'�� ��ȭ ��ȸ"
				+ "\n���� ����ȣ ���� ����");
		String[] condition = inputText.split(" ");
		if (condition.length != 4) {
			JOptionPane.showMessageDialog(null, "��Ŀ� ���� �Է��� �ּ���!", "���� �޽���", JOptionPane.WARNING_MESSAGE);
			return;
		}
		// ������� �Է� ���ǿ� ���� sql�� ����
		for (int i = 0; i < 4; ++i) {
			if (condition[i].equals("����")) continue;
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
		
		//movie_id�� �ʱ�ȭ
		movie_id = -1;
		row = -1;
		JFrame movieJf = new JFrame("��ȭ ��ȸ");
		JPanel mini = new JPanel();
		JLabel label = new JLabel("�˻��� ���ǿ� �����ϴ� ��ȭ ��ȸ");
				
		//��ȭ ����Ʈ�� ���̺�� �ҷ�����
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
	
	// searchMovie���� ��ȸ�� ��ȭ�� ���� ���� ���
	public void movieReservation() {
		if (row == -1) {
			JOptionPane.showMessageDialog(null, "��ȭ�� ������ �ּ���!", "���� �޽���", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		JPanel reservPanel = new JPanel();
		JPanel confirmPanel = new JPanel();
		JLabel confirmLabel = new JLabel();
		String query = "", selectedTime = "";
		String[] method = {"�ſ�ī��", "�������Ա�", "�޴���", "īī������", "���̹�����"};
		String[] ticketCount = {"1", "2", "3", "4"};
		ArrayList<String> schedule_time = new ArrayList<String> ();
		ArrayList<Integer> schedule_id = new ArrayList<Integer>();
		ArrayList<Integer> theater_id = new ArrayList<Integer>();
		int seat_num, selectedSchedule, result, selectedTheater, seat_id;
		String pay_method = "";
		
		//�������� Ȯ���� ���� ���� ��¥
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
		String todayDate = today.format(formatter);
		
		//������ ��ȭ�� ���� �� ������ �������� �޺��ڽ� ����
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
				
		// ���� �ð�(=�󿵰�) �� �������� ����
		JComboBox scheduleCombo = new JComboBox(schedule_time.toArray(new String[schedule_time.size()]));
		JComboBox methodCombo = new JComboBox(method);
		JComboBox ticketCombo = new JComboBox(ticketCount);
				
		reservPanel.add(new JLabel("���� �ð��� �����ϼ���: "));
		reservPanel.add(scheduleCombo);
		reservPanel.add(Box.createHorizontalStrut(10));
		reservPanel.add(new JLabel("���� ������ ������ �ּ���: "));
		reservPanel.add(methodCombo);
		reservPanel.add(new JLabel("�����Ͻ� Ƽ���� ���� ������ �ּ���: "));
		reservPanel.add(ticketCombo);
		result = JOptionPane.showConfirmDialog(null, reservPanel, "��¥ �� ��������� ������ �ּ���.", JOptionPane.OK_CANCEL_OPTION);
		
		// ����ڰ� ������ ��¥�� ������� schedule_id �޾ƿ�
		selectedTime = scheduleCombo.getSelectedItem().toString();
		int idx = schedule_time.indexOf(selectedTime);
		selectedSchedule = schedule_id.get(idx);
		pay_method = methodCombo.getSelectedItem().toString();
		int ticket_count = Integer.parseInt(ticketCombo.getSelectedItem().toString());
		int pay_amount = ticket_count * ticket_price;
		
		// �󿵰� ���� �����ָ� ���� Ȯ��
		selectedTheater = theater_id.get(idx);
		confirmPanel.add(new JLabel("�� �ð� : "));
		confirmPanel.add(new JLabel(selectedTime));
		reservPanel.add(Box.createHorizontalStrut(10));
		confirmPanel.add(new JLabel(", �󿵰� : "));
		confirmPanel.add(new JLabel(Integer.toString(selectedTheater)));
		
		result = JOptionPane.showConfirmDialog(null, confirmPanel, "�����Ͻ� ������ �󿵰��� Ȯ���� �ּ���. �����Ͻðڽ��ϱ�?", JOptionPane.OK_CANCEL_OPTION);
		
		if (result == JOptionPane.YES_OPTION) {
			
			int reservation_id = 0, ticket_id = 0;
			
			//������ Ʃ���� reservation_id�� ���� ���ڸ� id�� ����
			reservation_id = getPK("reservation");
			
			String sql[] = {"INSERT INTO Reservation VALUES("};
			sql[0] += Integer.toString(reservation_id) + ", '" + pay_method + "', '���� �Ϸ�', '" + Integer.toString(pay_amount) + "', '" + todayDate + "', " + User_id + ");";
			System.out.println(sql[0]);
			executeSQL(sql);
			
			//������� �������
			
			// Ƽ�� ���ڸ�ŭ 'Ƽ��' ����
			for (int i = 0; i < ticket_count; ++i) {
				//������ Ʃ���� ticket_id�� ���� ���ڸ� id�� ����
				ticket_id = getPK("ticket");
				
				//����ڰ� ������ �󿵰��� �� �¼� �� ù ���� �¼� ��������
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
				
				//����� �¼���ȣ ����
				System.out.println(seat_id);
				sql[0] = "UPDATE Seat SET seat_use = 'Y' WHERE seat_id =";
				sql[0] += Integer.toString(seat_id) + ";";
				executeSQL(sql);
				
				// ���ſ� ���� Ƽ������ �ڵ� ����
				sql[0] = "INSERT INTO Ticket VALUES(";
				sql[0] += Integer.toString(ticket_id) + ", '�߱����� ����', '" + Integer.toString(ticket_price) + "', '" + Integer.toString(ticket_price) + "', "
						+ Integer.toString(selectedTheater) + ", " + Integer.toString(selectedSchedule) + ", " + Integer.toString(seat_id) + ", " + Integer.toString(reservation_id) + ");";
				System.out.println(sql[0]);
				executeSQL(sql);
			}
			
		}
		else {
			JOptionPane.showMessageDialog(null, "���Ű� ��ҵǾ����ϴ�.", "���", JOptionPane.ERROR_MESSAGE);
		}
	}

	// ������� ���� primary key�� �ڵ� ��ȯ
	int getPK(String table) {
		
		String id = table + "_id";
		int last_id = 0;
		//������ Ʃ���� PK�� ���� ���ڸ� id�� ����
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
		JFrame reservastionJf = new JFrame("���� ��Ȳ ���");
		JPanel mini = new JPanel();
		JPanel bottomPanel = new JPanel();
		
		row = -1;
		// ���� ��Ȳ�� ���̺�� �ҷ�����
		reservationTable = getUserReservationTable();
		reservationTable.addMouseListener(this);
		
		//getTable �޼ҵ带 �̿��� ���̺��� �޾ƿ���
		JScrollPane tableData = new JScrollPane(getUserReservationTable(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
		
		String query = "select Re.reservation_id, Mo.movie_name, Sc.date, Ti.theater_id, Ti.seat_id, Ti.price from reservation as Re, ticket as Ti, schedule as Sc, Movie as Mo" 
				+ " where Re.reservation_id = Ti.reservation_id and Ti.schedule_id = Sc.schedule_id and Sc.movie_id = Mo.movie_id;";
		String countQuery = "SELECT COUNT(*) from "
				+ "(select Re.reservation_id, Mo.movie_name, Sc.date, Ti.theater_id, Ti.seat_id, Ti.price "
				+ "from reservation as Re, ticket as Ti, schedule as Sc, Movie as Mo" 
				+ " where Re.reservation_id = Ti.reservation_id and Ti.schedule_id = Sc.schedule_id and Sc.movie_id = Mo.movie_id) as WT;";
		int i = 0;
		
		//�Ӽ��� �޾ƿ���
		String columnName[] = {"���Ź�ȣ", "��ȭ��", "����", "�󿵰���ȣ", "�¼���ȣ", "�ǸŰ���"};
		
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
			System.out.println(count);
			//�����ͺ��̽� ũ�⸸ŭ�� 2���� �迭 ����
			String data[][] = new String[count][columnName.length];
			
			//2���� �迭 data�� table ��ȸ�ϸ� ������ ����
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
	
	public void deleteReserve() {
		if (row == -1) {
			JOptionPane.showMessageDialog(null, "��ȭ�� ������ �ּ���!", "���� �޽���", JOptionPane.WARNING_MESSAGE);
			return;
		}
		String[] query = {"DELETE FROM Reservation WHERE "};
	}
	
	
	public void mouseClicked(MouseEvent e) {
		JTable table = (JTable)e.getComponent();
		TableModel model = (TableModel)table.getModel();
		column = table.getSelectedColumn();
		row = table.getSelectedRow();
		System.out.println(row);
		
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
	}


	
}


