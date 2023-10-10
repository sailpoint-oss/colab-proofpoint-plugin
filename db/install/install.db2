--
-- DB2 DDL plugin script
--

-- Table to store settings
CREATE TABLE iplus_proofpoint_settings (
    app_list CLOB,
    vap_list CLOB,
    last_updated BIGINT
);

INSERT INTO iplus_proofpoint_settings (app_list, vap_list, last_updated) VALUES ('[]', '[]', (CAST(DAYS(CURRENT TIMESTAMP) -DAYS('1970-01-01-00:00:00.00000') AS DOUBLE) * 86400 + MIDNIGHT_SECONDS(CURRENT TIMESTAMP)) * 1000 + MICROSECOND(CURRENT TIMESTAMP)/1000);

-- Table to store data for pw reset & access control requests 
CREATE TABLE iplus_proofpoint_data (
    id VARCHAR(36) NOT NULL,
    identity_id VARCHAR(128) NOT NULL,
    email_id VARCHAR(512) NOT NULL,
    type VARCHAR(128),
    result CLOB,
    status VARCHAR(16),
    message VARCHAR(512),
    created BIGINT,
    last_updated BIGINT,
    PRIMARY KEY (id) 
);

CREATE INDEX idx_iplus_proofpoint_data_identity_id ON iplus_proofpoint_data (identity_id);
CREATE INDEX idx_iplus_proofpoint_data_email_id ON iplus_proofpoint_data (email_id);
CREATE INDEX idx_iplus_proofpoint_data_type ON iplus_proofpoint_data (type);
CREATE INDEX idx_iplus_proofpoint_data_status ON iplus_proofpoint_data (status); 
CREATE INDEX idx_iplus_proofpoint_data_created ON iplus_proofpoint_data (created);


-- Table to store data for VAP Users
CREATE TABLE iplus_proofpoint_vap_data (
    id VARCHAR(36) NOT NULL,
    identity_id VARCHAR(128) NOT NULL,
    email_id VARCHAR(512) NOT NULL,
    result CLOB,
    status VARCHAR(16),
    message VARCHAR(512),
    created BIGINT,
    last_updated BIGINT,
    PRIMARY KEY (id) 
);

CREATE INDEX idx_iplus_proofpoint_vap_data_identity_id ON iplus_proofpoint_vap_data (identity_id); 
CREATE INDEX idx_iplus_proofpoint_vap_data_email_id ON iplus_proofpoint_vap_data (email_id); 
CREATE INDEX idx_iplus_proofpoint_vap_data_status ON iplus_proofpoint_vap_data (status); 
CREATE INDEX idx_iplus_proofpoint_vap_data_created ON iplus_proofpoint_vap_data (created);
