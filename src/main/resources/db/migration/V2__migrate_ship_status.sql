-- Migration script to convert ship status from French to English enum values
-- This updates the CommercialShips table status column from String to enum-compatible values

UPDATE commercial_ships
SET status = CASE
    WHEN status = 'Au port' THEN 'IN_PORT'
    WHEN status = 'En mer' THEN 'AT_SEA'
    WHEN status = 'En escorte' THEN 'UNDER_ESCORT'
    WHEN status = 'En attente' THEN 'WAITING'
    WHEN status = 'En chargement' THEN 'LOADING'
    WHEN status = 'En déchargement' THEN 'UNLOADING'
    WHEN status = 'En maintenance' THEN 'IN_MAINTENANCE'
    WHEN status = 'En réparation' THEN 'IN_REPAIR'
    WHEN status = 'Autre' THEN 'OTHER'
    ELSE 'IN_PORT'  -- Default value for any other cases
END
WHERE status IS NOT NULL;

-- Set default value for any NULL status
UPDATE commercial_ships
SET status = 'IN_PORT'
WHERE status IS NULL;
