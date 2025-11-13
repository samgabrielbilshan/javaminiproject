import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class HospitalAppointmentSystem extends JFrame {
    private JComboBox<String> doctorBox, slotBox;
    private JTextField patientNameField, ageField;
    private JSpinner dateSpinner;
    private JButton checkButton, bookButton;
    private JPanel bookingPanel, summaryPanel;
    private CardLayout cardLayout;
    private String doctor, slot, patientName, date;
    private int patientAge;
    private String appointmentSummary;

    private final String[] doctors = {"Dr. Sharma", "Dr. Kumar", "Dr. Rao", "Dr. Mehta"};
    private final String[] slots = {"09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM"};

    private final Map<String, Set<String>> bookedSlots = new HashMap<>();
    private final DefaultListModel<String> bookingsListModel = new DefaultListModel<>();
    private JList<String> bookingsListComponent;

    public HospitalAppointmentSystem() {
        setTitle("Hospital Appointment Management System");
        setSize(600, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        for (String d : doctors) {
            bookedSlots.put(d, new HashSet<>());
        }

        cardLayout = new CardLayout();
        JPanel container = new JPanel(cardLayout);

        bookingPanel = new JPanel(new GridBagLayout());
        bookingPanel.setBackground(new Color(230, 245, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Appointment Booking");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        bookingPanel.add(title, gbc); gbc.gridwidth = 1;

        gbc.gridy = 1; gbc.gridx = 0; bookingPanel.add(new JLabel("Select Doctor"), gbc);
        gbc.gridx = 1; doctorBox = new JComboBox<>(doctors); bookingPanel.add(doctorBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2; bookingPanel.add(new JLabel("Select Time Slot"), gbc);
        gbc.gridx = 1; slotBox = new JComboBox<>(slots); bookingPanel.add(slotBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3; bookingPanel.add(new JLabel("Select Date"), gbc);
        gbc.gridx = 1;
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy");
        dateSpinner.setEditor(dateEditor);
        bookingPanel.add(dateSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 4; bookingPanel.add(new JLabel("Patient Name"), gbc);
        gbc.gridx = 1; patientNameField = new JTextField(15); bookingPanel.add(patientNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; bookingPanel.add(new JLabel("Age"), gbc);
        gbc.gridx = 1; ageField = new JTextField(5); bookingPanel.add(ageField, gbc);

        gbc.gridx = 0; gbc.gridy = 6; checkButton = new JButton("Check Availability"); bookingPanel.add(checkButton, gbc);
        gbc.gridx = 1; bookButton = new JButton("Book Appointment"); bookButton.setEnabled(false); bookingPanel.add(bookButton, gbc);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        JButton viewBookingsButton = new JButton("View All Bookings");
        bookingPanel.add(viewBookingsButton, gbc);
        gbc.gridwidth = 1;

        summaryPanel = new JPanel(new BorderLayout(10,10));
        summaryPanel.setBackground(new Color(240, 250, 240));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel summaryTitle = new JLabel("Appointment Summary", SwingConstants.CENTER);
        summaryTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        summaryPanel.add(summaryTitle, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(5,5));
        JLabel lastSummaryLabel = new JLabel();
        lastSummaryLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        centerPanel.add(lastSummaryLabel, BorderLayout.NORTH);

        bookingsListComponent = new JList<>(bookingsListModel);
        JScrollPane scroll = new JScrollPane(bookingsListComponent);
        centerPanel.add(scroll, BorderLayout.CENTER);

        summaryPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel summaryButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back to Booking");
        summaryButtons.add(backButton);
        JButton cancelSelectedButton = new JButton("Cancel Selected Booking");
        summaryButtons.add(cancelSelectedButton);
        summaryPanel.add(summaryButtons, BorderLayout.SOUTH);

        container.add(bookingPanel, "Booking");
        container.add(summaryPanel, "Summary");
        add(container);

        checkButton.addActionListener(e -> {
            doctor = doctorBox.getSelectedItem().toString();
            slot = slotBox.getSelectedItem().toString();
            patientName = patientNameField.getText();
            date = new java.text.SimpleDateFormat("dd-MM-yyyy").format((Date) dateSpinner.getValue());

            try {
                patientAge = Integer.parseInt(ageField.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter valid age!");
                return;
            }

            Set<String> booked = bookedSlots.get(doctor);
            String key = slot + "_" + date;
            if (booked.contains(key)) {
                JOptionPane.showMessageDialog(this, "Slot already booked for this doctor on " + date);
                bookButton.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(this, "Slot available for booking on " + date);
                bookButton.setEnabled(true);
            }
        });

        bookButton.addActionListener(e -> {
            doctor = doctorBox.getSelectedItem().toString();
            slot = slotBox.getSelectedItem().toString();
            patientName = patientNameField.getText();
            date = new java.text.SimpleDateFormat("dd-MM-yyyy").format((Date) dateSpinner.getValue());

            try {
                patientAge = Integer.parseInt(ageField.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter valid age!");
                return;
            }

            Set<String> booked = bookedSlots.get(doctor);
            String key = slot + "_" + date;
            if (booked.contains(key)) {
                JOptionPane.showMessageDialog(this, "Sorry, that slot is already booked on " + date);
                bookButton.setEnabled(false);
                return;
            }

            booked.add(key);
            appointmentSummary = String.format(
                "<html><b>Doctor:</b> %s<br><b>Date:</b> %s<br><b>Time Slot:</b> %s<br><b>Patient:</b> %s<br><b>Age:</b> %d</html>",
                doctor, date, slot, patientName, patientAge
            );

            String entry = String.format("Doctor: %s | Date: %s | Time: %s | Patient: %s | Age: %d", doctor, date, slot, patientName, patientAge);
            bookingsListModel.addElement(entry);

            lastSummaryLabel.setText(appointmentSummary);
            cardLayout.show(container, "Summary");
            bookButton.setEnabled(false);
        });

        viewBookingsButton.addActionListener(e -> {
            lastSummaryLabel.setText(bookingsListModel.isEmpty() ? "<html><i>No bookings yet</i></html>" : "");
            cardLayout.show(container, "Summary");
        });

        backButton.addActionListener(e -> cardLayout.show(container, "Booking"));

        cancelSelectedButton.addActionListener(e -> {
            int idx = bookingsListComponent.getSelectedIndex();
            if (idx == -1) {
                JOptionPane.showMessageDialog(this, "Select a booking to cancel.");
                return;
            }
            String selected = bookingsListModel.getElementAt(idx);
            String[] parts = selected.split("\\|");
            String docPart = parts[0].trim();
            String datePart = parts[1].trim();
            String timePart = parts[2].trim();

            String docName = docPart.substring(docPart.indexOf(":") + 1).trim();
            String dateStr = datePart.substring(datePart.indexOf(":") + 1).trim();
            String timeSlot = timePart.substring(timePart.indexOf(":") + 1).trim();

            Set<String> bookedSet = bookedSlots.get(docName);
            if (bookedSet != null) {
                bookedSet.remove(timeSlot + "_" + dateStr);
            }
            bookingsListModel.remove(idx);
            JOptionPane.showMessageDialog(this, "Booking cancelled for " + dateStr + ".");
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HospitalAppointmentSystem().setVisible(true));
    }
}