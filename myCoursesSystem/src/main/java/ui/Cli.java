package ui;

import dataaccess.DatabaseException;
import dataaccess.MyCourseRepository;
import dataaccess.MySqlCourseRepository;
import domain.Course;
import domain.CourseType;
import domain.InvalidValueException;

import javax.xml.crypto.Data;
import java.awt.image.AreaAveragingScaleFilter;
import java.sql.Date;
import java.util.ArrayList;
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
                    addcourse();
                    break;
                case "2":
                    showAllCourses();
                    System.out.println("Alle Kurse anzeigen");
                    break;
                case "3":
                    showCourseDetails();
                    System.out.println("Kursdetailsanzeigen");
                    break;
                case "4":
                    updateCourseDetails();
                    System.out.println("Kurs Details ändern");
                    break;
                case "5":
                    deleteCourse();
                    break;
                case "6":
                    courseSearch();
                    break;
                case "7":
                    runningCourses();
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

    private void runningCourses() {
        System.out.println("Aktuell laufende Kurse: ");
        List<Course> list;
        try {
            list = repository.findAllrunningCourses();
            for (Course course : list) {
                System.out.println(course);
            }
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei der Anzeige für laufende Kurse " + databaseException.getMessage());

        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler bei der Anzeige für laufende Kurse " + exception.getMessage());

        }
    }

    private void courseSearch() {
        System.out.println("Geben sie einen Suchbegriff an: ");
        String searchstring = scan.nextLine();
        List<Course> courseList;
        try {
            courseList = repository.findAllCoursesByNameOrDescription(searchstring);
            for (Course course : courseList) {
                System.out.println(course);
            }
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei der Kurssuche " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler bei der Kurssuche " + exception.getMessage());
        }
    }

    private void deleteCourse() {
        System.out.println("Welchen kurs möchten sie Löschen? Bitte ID eingeben: ");
        Long courseIdToDelete = Long.parseLong(scan.nextLine());
        System.out.println("Kurs mit der ID " + courseIdToDelete + " gelöscht!");

        try {
            repository.deleteById(courseIdToDelete);
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Löschen: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler beim Löschen: " + e.getMessage());
        }
    }

    private void updateCourseDetails() {
        System.out.println("Für welchen Kurs-ID möchten Sie die Kursdetails ändern?");
        Long courseId = Long.parseLong(scan.nextLine());
        try {
            Optional<Course> courseOptional = repository.getById(courseId);
            if (courseOptional.isEmpty()) {
                System.out.println("Kurs mit der gegebenen ID nicht in der Datenbank!");
            } else {
                Course course = courseOptional.get();
                System.out.println("Änderungen für folgenden Kurs: ");

                String name, description, hours, dateFrom, dateTo, courseType;

                System.out.println("Bitte neue Kursdaten angeben (Enter, falls keine Änderung gewünscht ist): ");
                System.out.println("Name: ");
                name = scan.nextLine();
                System.out.println("Beschreibung: ");
                description = scan.nextLine();
                System.out.println("Stundenanzahl: ");
                hours = scan.nextLine();
                System.out.printf("Startdatum (YYYY-MM-DD): ");
                dateFrom = scan.nextLine();
                System.out.println("Enddatum (YYYY-MM-DD): ");
                dateTo = scan.nextLine();
                System.out.println("Kurstyp (ZA/BF/FF/OE): ");
                courseType = scan.nextLine();

                Optional<Course> optionalCourseUpdated = repository.update(
                        new Course(
                                course.getId(),
                                name.equals("") ? course.getName() : name,
                                description.equals("") ? course.getDescription() : description,
                                hours.equals("") ? course.getHours() : Integer.parseInt(hours),
                                dateFrom.equals("") ? course.getBeginDate() : Date.valueOf(dateFrom),
                                dateTo.equals("") ? course.getEndDate() : Date.valueOf(dateTo),
                                courseType.equals("") ? course.getCourseType() : CourseType.valueOf(courseType)
                        )
                );
                optionalCourseUpdated.ifPresentOrElse(
                        (c) -> System.out.println("Kurs aktualisiert: " + c),
                        () -> System.out.println("Kurs konnte nicht aktualisiert werden!")
                );

            }
        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        } catch (InvalidValueException invalidValueExcpetion) {
            System.out.println("Kursdaten nicht korrekt angegeben: " + invalidValueExcpetion.getMessage());
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Updaten: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler beim Updaten: " + exception.getMessage());
        }
    }

    private void addcourse() {
        String name, description;
        int hours;
        Date dateFrom, dateTo;
        CourseType courseType;
        try {
            System.out.println("Bitte alle Kursdaten angeben:");
            System.out.println("Name: ");
            name = scan.nextLine();
            if (name.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");
            System.out.println("Beschreibung: ");
            description = scan.nextLine();
            if (description.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");
            System.out.println("Stundenanzahl: ");
            hours = Integer.parseInt(scan.nextLine());
            System.out.println("Startdatum (YYYY-MM-DD): ");
            dateFrom = Date.valueOf(scan.nextLine());
            System.out.println("Enddatum (YYYY-MM-DD): ");
            dateTo = Date.valueOf(scan.nextLine());
            System.out.println("Kurstyp: (ZA/BF/FF/OE): ");
            courseType = CourseType.valueOf(scan.nextLine());
            Optional<Course> optionalCourse = repository.insert(
                    new Course(name,
                            description,
                            hours,
                            dateFrom,
                            dateTo,
                            courseType
                    ));

            if (optionalCourse.isPresent()) {
                System.out.println("Kurs angelegt: " + optionalCourse.get());
            } else {
                System.out.println("Kurs konnte nicht angelegt werden!");
            }

        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        } catch (InvalidValueException invalidValueException) {
            System.out.println("Kursdaten nicht korrekt angegeben: " + invalidValueException.getMessage());
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim einfügen: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler beim einfügen " + exception.getMessage());
        }


    }

    private void showCourseDetails() {
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
        System.out.println("(1) Kurs einegeben \t (2) Alle Kurse anzeigen \t (3) Kursdetails anzeigen\n");
        System.out.println("(4) Kurs Detailsändern \t (5) Kurs löschen \t (6) Kursuche  ");
        System.out.println("(7) Laufende Kurse \t(x) Ende");

    }

    private void inputError() {
        System.out.println("Bitte nur die Zahlen der Menueauswahl eingeben!");
    }
}
