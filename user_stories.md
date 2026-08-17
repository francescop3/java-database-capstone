# User Story Template

**Title:**
_As a [user role], I want [feature/goal], so that [reason]._

**Acceptance Criteria:**
1. [Criteria 1]
2. [Criteria 2]
3. [Criteria 3]

**Priority:** [High/Medium/Low]
**Story Points:** [Estimated Effort in Points]
**Notes:**
- [Additional information or edge cases]


## Admin User Stories

**Title:**
_As a admin, I want to log in to the platform, so that I can manage the platform securely._

**Acceptance Criteria:**
1. Admin can enter username and password on the login page.
2. Admin is redirected to the platform after successful login.
3. An error message is displayed for invalid credentials.

**Priority:** High
**Story Points:** 3
**Notes:**
- Passwords should be stored using a secure hashing algorithm.


**Title:**
_As an admin, I want to log out of the portal, so that I can protect the system access when I'm done on the platform._

**Acceptance Criteria:**
1. A visible "Logout" option is available from the platform.
2. Clicking "Logout" ends the admin's session/token immediately.
3. Attempting to access admin pages after logout redirects to the login page.

**Priority:** High
**Story Points:** 2
**Notes:**
- Session should expire automatically after a period of inactivity.


**Title:**
_As a admin, I want to add doctors, so that they can be listed and made available for patient appointments._

**Acceptance Criteria:**
1. Admin can access an "Add Doctor" form from the dashboard.
2. Form requires fields such as name, specialization, contact info, and credentials.
3. On successful submission, the doctor's profile is created and stored in the database.

**Priority:** High
**Story Points:** 4
**Notes:**
- Consider validating unique fields to avoid duplicate profiles.


**Title:**
_As an admin, I want to delete a doctor from the portal, so that outdated or invalid records are removed from the system._

**Acceptance Criteria:**
1. Admin can search and select a doctor to delete.
2. Deletion option is available close to the doctor element.
3. Upon deletion, the doctor is permanently removed (or soft-deleted, per policy).

**Priority:** Medium
**Story Points:** 3
**Notes:**
- Ensure cascading effects on related data (appointments, patient records) are handled gracefully.


**Title:** 
_As an admin, I want to run a stored procedure in the MySQL CLI to get the number of appointments per month, so that I can track platform usage statistics._

**Acceptance Criteria:**
1. A stored procedure exists in the MySQL database that returns appointment counts grouped by month.
2. Admin can invoke the stored procedure via the MySQL CLI with the correct syntax.
3. Output displays month and corresponding appointment count.

**Priority:** Medium
**Story Points:** 4
**Notes:**
- Document the exact CALL syntax and required parameters (e.g., date range) for other admins/developers.


## Patient User Stories

**Title:** 
_As a patient, I want to view a list of doctors without logging in, so that I can explore my options before registering._

**Acceptance Criteria:**
1. Doctor list is publicly accessible without authentication.
2. Each doctor entry displays basic info (e.g., name, specialization, availability).
3. Patient cannot access booking or profile details without logging in.

**Priority:** High
**Story Points:** 2
**Notes:**
- Consider adding filters for easier browsing.
- Sensitive doctor info should remain hidden until login.


**Title:**
_As a patient, I want to sign up using my email and password, so that I can book appointments._

**Acceptance Criteria:**
1. Sign-up form requires email, password, and basic profile details (e.g., name, phone number).
2. System validates email format and password strength.
3. System checks for duplicate accounts using the same email.

**Priority:** High
**Story Points:** 3
**Notes:**
- Consider email verification as a required step before allowing bookings.


**Title:** 
_As a patient, I want to log into the portal, so that I can manage my bookings._

**Acceptance Criteria:**
1. Patient can enter email and password on the login page.
2. Patient is redirected to their dashboard upon successful login.
3. An error message is displayed for invalid credentials.

**Priority:** High
**Story Points:** 3
**Notes:**
- Consider a "Forgot Password" flow for account recovery.


**Title:** 
_As a patient, I want to log in and book an hour-long appointment, so that I can consult with a doctor._

**Acceptance Criteria:**
1. Patient must be logged in to access the booking flow.
2. Patient can select a doctor, date, and available one-hour time slot.
3. Booking is confirmed and saved to the patient's account.

**Priority:** High
**Story Points:** 5
**Notes:**
- Consider preventing double-booking of the same slot by another patient.


**Title:** 
_As a patient, I want to view my upcoming appointments, so that I can prepare accordingly._

**Acceptance Criteria:**
1. Patient can access a list of their upcoming appointments after logging in.
2. Each appointment displays doctor name, date, time, and duration.
3. Past appointments are excluded from this view.

**Priority:** Medium
**Story Points:** 2
**Notes:**
- A "Cancel" or "Reschedule" option here could be useful.


## Doctor User Stories

**Title:**
_As a doctor, I want to log into the portal, so that I can manage my appointments._

**Acceptance Criteria:**
1. Doctor can enter username/email and password on the login page.
2. Doctor is redirected to their dashboard upon successful login.
3. An error message is displayed for invalid credentials.

**Priority:** High
**Story Points:** 3
**Notes:**
- Consider a "Forgot Password" flow for account recovery.


**Title:** 
_As a doctor, I want to view my appointment calendar, so that I can stay organized._

**Acceptance Criteria:**
1. Doctor can access a calendar view showing all scheduled appointments.
2. Calendar displays appointment date, time, duration, and patient name.
3. Calendar updates in real time (or near real time) as new appointments are booked or cancelled.

**Priority:** High
**Story Points:** 5
**Notes:**
- Consider color-coding appointment statuses (confirmed, pending, cancelled).


**Title:** 
_As a doctor, I want to mark my unavailability, so that patients only see and book available slots._

**Acceptance Criteria:**
1. Doctor can select specific dates/times to mark as unavailable.
2. Marked unavailable slots are immediately removed from patient-facing booking options.
3. Doctor can edit or remove previously marked unavailability.

**Priority:** High
**Story Points:** 5
**Notes:**
- Define how conflicts with existing bookings should be handled.


**Title:**
_As a doctor, I want to update my profile, so that patients have up-to-date information._

**Acceptance Criteria:**
1. Doctor can access an editable profile page with fields for specialization, contact info, and other relevant details.
2. System validates required fields and correct formats.
3. Changes are saved and reflected immediately in the patient-facing doctor listing.

**Priority:** Medium
**Story Points:** 3
**Notes:**
- Consider requiring admin approval for sensitive changes.


**Title:** 
_As a doctor, I want to view the patient details for upcoming appointments, so that I can be prepared._

**Acceptance Criteria:**
1. Doctor can select an upcoming appointment to view associated patient details.
2. Details include patient name, contact info, and relevant medical history/notes if available.
3. Access to patient details is restricted to appointments assigned to that doctor.

**Priority:** Medium
**Story Points:** 3
**Notes:**
- Ensure patient data handling complies with relevant privacy regulations (e.g., HIPAA/GDPR).