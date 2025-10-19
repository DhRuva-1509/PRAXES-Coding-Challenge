-- Sample data for Consultation 1: Patient P123 with Doctor D456
INSERT INTO consultations (patient_id, doctor_id, created_at)
VALUES ('P123', 'D456', '2025-10-19 09:00:00');

-- Sample data for Consultation 2: Patient P789 with Doctor D012
INSERT INTO consultations (patient_id, doctor_id, created_at)
VALUES ('P789', 'D012', '2025-10-19 10:00:00');

-- Messages for Consultation 1 (about headaches)
INSERT INTO messages (consultation_id, author, author_role, content, timestamp) VALUES
    (1, 'P123', 'PATIENT', 'Hello Doctor, I have been experiencing severe headaches for the past week.', '2025-10-19 09:05:00'),
    (1, 'D456', 'DOCTOR', 'Hello! I am sorry to hear that. Can you describe the type of pain you are experiencing?', '2025-10-19 09:07:00'),
    (1, 'P123', 'PATIENT', 'It feels like a throbbing pain on the right side of my head.', '2025-10-19 09:10:00'),
    (1, 'D456', 'DOCTOR', 'Have you experienced any visual disturbances or sensitivity to light?', '2025-10-19 09:12:00'),
    (1, 'P123', 'PATIENT', 'Yes, bright lights make it worse, and I sometimes see spots.', '2025-10-19 09:15:00'),
    (1, 'D456', 'DOCTOR', 'This sounds like it could be migraines. I will prescribe some medication and we should schedule a follow-up.', '2025-10-19 09:20:00');

-- Messages for Consultation 2 (about medication side effects)
INSERT INTO messages (consultation_id, author, author_role, content, timestamp) VALUES
    (2, 'P789', 'PATIENT', 'Hi Doctor, I wanted to discuss some side effects from the medication you prescribed last week.', '2025-10-19 10:05:00'),
    (2, 'D012', 'DOCTOR', 'Of course. What symptoms have you been experiencing?', '2025-10-19 10:08:00'),
    (2, 'P789', 'PATIENT', 'I have been feeling quite nauseous, especially in the mornings.', '2025-10-19 10:12:00'),
    (2, 'D012', 'DOCTOR', 'That is a known side effect. Have you been taking it with food?', '2025-10-19 10:15:00'),
    (2, 'P789', 'PATIENT', 'I have been taking it on an empty stomach. Should I take it with meals?', '2025-10-19 10:18:00'),
    (2, 'D012', 'DOCTOR', 'Yes, definitely take it with food. That should help reduce the nausea significantly.', '2025-10-19 10:22:00'),
    (2, 'P789', 'PATIENT', 'Thank you! Should I continue with the same dosage?', '2025-10-19 10:25:00'),
    (2, 'D012', 'DOCTOR', 'Yes, continue with the same dosage but with food. Contact me if the nausea persists after 3 days.', '2025-10-19 10:28:00');
