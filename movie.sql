-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema madang
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema madang
-- -----------------------------------------------------
DROP DATABASE IF EXISTS  madang;
DROP USER IF EXISTS  madang@localhost;
create user madang@localhost identified WITH mysql_native_password  by 'madang';
create database madang;
grant all privileges on madang.* to madang@localhost with grant option;
commit;

USE madang;
-- -----------------------------------------------------
-- Table `madang`.`Movie`
-- -----------------------------------------------------
CREATE TABLE Movie (
  `movie_id` INT NOT NULL,
  `movie_name` VARCHAR(45) NULL,
  `screentime` VARCHAR(45) NULL,
  `rating` VARCHAR(45) NULL,
  `director` VARCHAR(45) NULL,
  `actor` VARCHAR(45) NULL,
  `genre` VARCHAR(45) NULL,
  `introduce` VARCHAR(45) NULL,
  `release` VARCHAR(45) NULL,
  PRIMARY KEY (`movie_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `madang`.`Theater`
-- -----------------------------------------------------
CREATE TABLE Theater (
  `theater_id` INT NOT NULL,
  `seat_num` INT NULL,
  `theater_use` VARCHAR(45) NULL,
  PRIMARY KEY (`theater_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `madang`.`Schedule`
-- -----------------------------------------------------
CREATE TABLE Schedule (
  `schedule_id` INT NOT NULL,
  `date` VARCHAR(45) NULL,
  `day` VARCHAR(45) NULL,
  `round` VARCHAR(45) NULL,
  `time` VARCHAR(45) NULL,
  `movie_id` INT NOT NULL,
  `theater_id` INT NOT NULL,
  PRIMARY KEY (`schedule_id`),
  INDEX `fk_Schedule_Movie1_idx` (`movie_id` ASC) VISIBLE,
  INDEX `fk_Schedule_Theater1_idx` (`theater_id` ASC) VISIBLE,
  CONSTRAINT `fk_Schedule_Movie1`
    FOREIGN KEY (`movie_id`)
    REFERENCES `madang`.`Movie` (`movie_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Schedule_Theater1`
    FOREIGN KEY (`theater_id`)
    REFERENCES `madang`.`Theater` (`theater_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `madang`.`Seat`
-- -----------------------------------------------------
CREATE TABLE Seat (
  `seat_id` INT NOT NULL,
  `seat_use` VARCHAR(45) NULL,
  `theater_id` INT NOT NULL,
  PRIMARY KEY (`seat_id`),
  INDEX `fk_Seat_Theater1_idx` (`theater_id` ASC) VISIBLE,
  CONSTRAINT `fk_Seat_Theater1`
    FOREIGN KEY (`theater_id`)
    REFERENCES `madang`.`Theater` (`theater_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `madang`.`Member`
-- -----------------------------------------------------
CREATE TABLE Member (
  `member_id` INT NOT NULL,
  `member_name` VARCHAR(45) NULL,
  `phone` VARCHAR(45) NULL,
  `mail` VARCHAR(45) NULL,
  PRIMARY KEY (`member_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `madang`.`Reservation`
-- -----------------------------------------------------
CREATE TABLE Reservation (
  `reservation_id` INT NOT NULL,
  `pay_method` VARCHAR(45) NULL,
  `pay_status` VARCHAR(45) NULL,
  `pay_amount` VARCHAR(45) NULL,
  `pay_date` VARCHAR(45) NULL,
  `member_id` INT NOT NULL,
  PRIMARY KEY (`reservation_id`),
  INDEX `fk_Reservation_Member1_idx` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_Reservation_Member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `madang`.`Member` (`member_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `madang`.`Ticket`
-- -----------------------------------------------------
CREATE TABLE Ticket (
  `ticket_id` INT NOT NULL,
  `issue` VARCHAR(45) NULL,
  `std_price` VARCHAR(45) NULL,
  `price` VARCHAR(45) NULL,
  `seat_id` INT NOT NULL,
  `schedule_id` INT NOT NULL,
  `reservation_id` INT NOT NULL,
  `theater_id` INT NOT NULL,
  PRIMARY KEY (`ticket_id`),
  INDEX `fk_Ticket_Seat1_idx` (`seat_id` ASC) VISIBLE,
  INDEX `fk_Ticket_Schedule1_idx` (`schedule_id` ASC) VISIBLE,
  INDEX `fk_Ticket_Reservation1_idx` (`reservation_id` ASC) VISIBLE,
  INDEX `fk_Ticket_Theater1_idx` (`theater_id` ASC) VISIBLE,
  CONSTRAINT `fk_Ticket_Seat1`
    FOREIGN KEY (`seat_id`)
    REFERENCES `madang`.`Seat` (`seat_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Ticket_Schedule1`
    FOREIGN KEY (`schedule_id`)
    REFERENCES `madang`.`Schedule` (`schedule_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Ticket_Reservation1`
    FOREIGN KEY (`reservation_id`)
    REFERENCES `madang`.`Reservation` (`reservation_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Ticket_Theater1`
    FOREIGN KEY (`theater_id`)
    REFERENCES `madang`.`Theater` (`theater_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

INSERT INTO Movie VALUES(3, '영화이름', '상영시간', '등급', '감독', '배우', '장르', '소개', '개봉일');

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
