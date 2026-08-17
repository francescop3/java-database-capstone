## MySQL Database Design

### Table: patients
- id: INT, Primary Key, Auto Increment
- first_name: VARCHAR(50), Not Null
- last_name: VARCHAR(50), Not Null
- email: VARCHAR(100), Not Null, Unique
- password_hash: VARCHAR(255), Not Null
- phone: VARCHAR(20), Nullable
- date_of_birth: DATE, Nullable
- created_at: TIMESTAMP, Not Null, Default CURRENT_TIMESTAMP
- is_active: BOOLEAN, Not Null, Default TRUE

### Table: doctors
- id: INT, Primary Key, Auto Increment
- first_name: VARCHAR(50), Not Null
- last_name: VARCHAR(50), Not Null
- email: VARCHAR(100), Not Null, Unique
- password_hash: VARCHAR(255), Not Null
- phone: VARCHAR(20), Nullable
- specialization: VARCHAR(100), Not Null
- license_number: VARCHAR(50), Not Null, Unique
- clinic_location_id: INT, Foreign Key → clinic_locations(id), Nullable
- created_at: TIMESTAMP, Not Null, Default CURRENT_TIMESTAMP
- is_active: BOOLEAN, Not Null, Default TRUE

### Table: admin
- id: INT, Primary Key, Auto Increment
- username: VARCHAR(50), Not Null, Unique
- email: VARCHAR(100), Not Null, Unique
- password_hash: VARCHAR(255), Not Null
- created_at: TIMESTAMP, Not Null, Default CURRENT_TIMESTAMP
- is_active: BOOLEAN, Not Null, Default TRUE

### Table: appointments
- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key → doctors(id), Not Null
- patient_id: INT, Foreign Key → patients(id), Not Null
- appointment_time: DATETIME, Not Null
- duration_minutes: INT, Not Null, Default 60
- status: INT, Not Null, Default 0 — (0 = Scheduled, 1 = Completed, 2 = Cancelled)
- created_at: TIMESTAMP, Not Null, Default CURRENT_TIMESTAMP

### Table: clinic_locations
- id: INT, Primary Key, Auto Increment
- name: VARCHAR(100), Not Null
- address: VARCHAR(255), Not Null
- city: VARCHAR(100), Not Null
- phone: VARCHAR(20), Nullable

### Table: payments
- id: INT, Primary Key, Auto Increment
- appointment_id: INT, Foreign Key → appointments(id), Not Null, Unique
- amount: DECIMAL(10,2), Not Null
- payment_method: VARCHAR(30), Not Null
- payment_status: INT, Not Null, Default 0 — (0 = Pending, 1 = Paid, 2 = Refunded, 3 = Failed)
- paid_at: TIMESTAMP, Nullable


## MongoDB Collection Design

### Collection: prescriptions

```json
{
  "_id": "ObjectId('64abc123456')",
  "appointmentId": 51,
  "patientId": 12,
  "doctorId": 7,
  "patientName": "John Smith",
  "doctorName": "Dr. Aisha Khan",
  "medications": [
    {
      "name": "Paracetamol",
      "dosage": "500mg",
      "frequency": "Every 6 hours",
      "durationDays": 5,
      "refillCount": 2
    },
    {
      "name": "Amoxicillin",
      "dosage": "250mg",
      "frequency": "Twice daily",
      "durationDays": 7,
      "refillCount": 0
    }
  ],
  "doctorNotes": "Take with food. Monitor for allergic reaction.",
  "pharmacy": {
    "name": "Walgreens SF",
    "location": "Market Street",
    "phone": "+1-415-555-0199"
  },
  "tags": ["antibiotic", "short-term", "follow-up-required"],
  "metadata": {
    "createdAt": "2026-08-17T09:32:00Z",
    "createdBy": "system",
    "source": "in-clinic visit",
    "version": 1
  },
  "status": "active"
}