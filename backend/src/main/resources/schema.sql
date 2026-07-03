DROP TABLE IF EXISTS batch_spools CASCADE;
DROP TABLE IF EXISTS spool_materials CASCADE;
DROP TABLE IF EXISTS spools CASCADE;
DROP TABLE IF EXISTS batches CASCADE;
DROP TABLE IF EXISTS isos CASCADE;
DROP TABLE IF EXISTS material_stock CASCADE;

-- 1. material_stock
CREATE TABLE material_stock (
                                id BIGSERIAL PRIMARY KEY,
                                code VARCHAR(100) UNIQUE NOT NULL,
                                description VARCHAR(255) NOT NULL,
                                quantity NUMERIC(10, 2) NOT NULL CHECK (quantity >= 0),
                                version BIGINT DEFAULT 0
);

-- 2. isos
CREATE TABLE isos (
                      id BIGSERIAL PRIMARY KEY,
                      iso_no VARCHAR(100) UNIQUE NOT NULL,
                      revision VARCHAR(50) NOT NULL,
                      status VARCHAR(50) NOT NULL -- e.g., 'RELEASED', 'IN_PROGRESS'
);

-- 3. batches
CREATE TABLE batches (
                         id BIGSERIAL PRIMARY KEY,
                         batch_no VARCHAR(100) UNIQUE NOT NULL,
                         created_date TIMESTAMP NOT NULL
);

-- 4. spools
CREATE TABLE spools (
                        id BIGSERIAL PRIMARY KEY,
                        spool_no VARCHAR(100) UNIQUE NOT NULL,
                        iso_id BIGINT NOT NULL,
                        status VARCHAR(50) NOT NULL, -- 'PENDING', 'BATCHED', 'PENDING_MATERIAL'
                        CONSTRAINT fk_spools_iso FOREIGN KEY (iso_id) REFERENCES isos(id) ON DELETE CASCADE
);

-- 5. spool_materials (Join table with extra column for required quantity)
CREATE TABLE spool_materials (
                                 id BIGSERIAL PRIMARY KEY,
                                 spool_id BIGINT NOT NULL,
                                 material_id BIGINT NOT NULL,
                                 qty_required NUMERIC(10, 2) NOT NULL CHECK (qty_required > 0),
                                 CONSTRAINT fk_sm_spool FOREIGN KEY (spool_id) REFERENCES spools(id) ON DELETE CASCADE,
                                 CONSTRAINT fk_sm_material FOREIGN KEY (material_id) REFERENCES material_stock(id) ON DELETE RESTRICT
);

-- 6. batch_spools (Explicit relationship table requested by the prompt)
CREATE TABLE batch_spools (
                              id BIGSERIAL PRIMARY KEY,
                              batch_id BIGINT NOT NULL,
                              spool_id BIGINT NOT NULL UNIQUE, -- Unique because a spool belongs to one batch
                              CONSTRAINT fk_bs_batch FOREIGN KEY (batch_id) REFERENCES batches(id) ON DELETE CASCADE,
                              CONSTRAINT fk_bs_spool FOREIGN KEY (spool_id) REFERENCES spools(id) ON DELETE CASCADE
);