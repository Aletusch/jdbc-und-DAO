package ui;

import dataaccess.DatabaseException;
import dataaccess.MyCourseRepository;
import dataaccess.MySqlCourseRepository;
import domain.Course;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Cli {
    Scanner scan;
    MyCourseRepository repository;

    public Cli(MySqlCourseRepository repository) {
        this.scan = new Scanner(System.in);
        this.repository = repository;
    }

    public void start() {
        String input = "-";
        while (!input.equals("x")) {

            showMenue();
            input = this.scan.nextLine();
            switch (input) {
                case "1":
                    System.out.println("Kurseingabe");
                    break;
                case "2":
                    showAllCourses();
                    System.out.println("Alle Kurse anzeigen");
                    break;
                case "3":
                    showCoursDetails();
                    System.out.println("Kursdetailsanzeigen");
                    break;
                case "x":
                    System.out.println("Auf Wiedersehen");
                    break;
                default:
                    inputError();
                    break;
            }
        }
        scan.close();
    }

    private void showCoursDetails() {
        System.out.println("Für welchen Kurs möchten Sie die Kursdetails anzeigen?");
        Long courseId = Long.parseLong(scan.nextLine());
        try {
            Optional<Course> courseOptional = repository.getById(courseId);
            if (courseOptional.isPresent()) {
                System.out.println(courseOptional.get());
            } else {
                System.out.println("Kurs mit der ID " + courseId + " nicht gefunden!");
            }
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei Kurs-Detailanzeige: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler bei Kurs-Detailanzeige: " + exception.getMessage());
        }

    }

    private void showAllCourses() {
        List<Course> list = null;
        try {
            list = repository.getAll();
            if (list.size() > 0) {
                for (Course course : list) {
                    System.out.println(course);
                }
            } else {
                System.out.println("Kursliste leer!");
            }
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei Anzeige aller Kurse: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler beim anzeigen der Kurse: " + exception.getMessage());
        }
    }

    private void showMenue() {
        System.out.println("--------------Kursmanagement--------------");
        System.out.println("(1) Kurs einegeben \t (2) Alle Kurse anzeigen \t (3) Kursdetails anzeigen\t");
        System.out.println("(x) Ende");

    }

    private void inputError() {
        System.out.println("Bitte nur die Zahlen der Menueauswahl eingeben!");
    }
}
