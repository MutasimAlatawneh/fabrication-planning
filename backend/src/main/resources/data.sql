-- 1. Insert Raw Materials into material_stock (code, description, quantity)
INSERT INTO material_stock (code, description, quantity) VALUES
                                                             ('PIPE-CS-001', 'Carbon Steel Pipe 2 inch', 50.00),
                                                             ('VALVE-BALL-02', 'Ball Valve 2 inch', 2.00),
                                                             ('BOLT-M16', 'Hex Bolt M16', 100.00);

-- 2. Insert an ISO into isos (iso_no, revision, status)
INSERT INTO isos (iso_no, revision, status) VALUES
    ('ISO-2026-A01', 'Rev 0', 'RELEASED');

-- 3. Insert Spools into spools (spool_no, iso_id, status)
INSERT INTO spools (spool_no, iso_id, status) VALUES
                                                  ('SPL-001', 1, 'PENDING'),        -- This one will pass batching (needs 10 pipes, 10 bolts)
                                                  ('SPL-002', 1, 'PENDING');        -- This one will fail batching (needs 3 valves, we only have 2)

-- 4. Insert Material Requirements into spool_materials (spool_id, material_id, qty_required)
-- SPL-001 needs 10m pipe (id: 1) and 10 bolts (id: 3)
INSERT INTO spool_materials (spool_id, material_id, qty_required) VALUES
                                                                      (1, 1, 10.00),
                                                                      (1, 3, 10.00);

-- SPL-002 needs 5m pipe (id: 1) and 3 valves (id: 2)
INSERT INTO spool_materials (spool_id, material_id, qty_required) VALUES
                                                                      (2, 1, 5.00),
                                                                      (2, 2, 3.00);