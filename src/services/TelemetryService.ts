export class TelemetryService {
  /**
   * Extracts standard context (payload size, operation type, etc) from raw BLE errors.
   */
  static extractBleContext(payload: Record<string, any>): Record<string, any> {
    const context: Record<string, any> = { ...payload };

    // Attempt to determine payload size if hex or payload buffer is present
    if (context.hex && typeof context.hex === 'string') {
      context.payload_size = Math.floor(context.hex.length / 2);
    } else if (context.payload && Array.isArray(context.payload)) {
      context.payload_size = context.payload.length;
    }

    // Attempt to determine operation type
    if (!context.operation_type) {
      if (context.writeType) {
        context.operation_type = context.writeType;
      } else if (context.type) {
        context.operation_type = context.type;
      } else if (context.hex) {
        context.operation_type = 'UNKNOWN_WRITE';
      }
    }

    // Extract status 133 explicitly if it exists in the error message
    const errorStr = String(context.error || context.message || '');
    if (errorStr.includes('status 133') || errorStr.includes('133')) {
      context.gatt_status = 133;
    }

    return context;
  }
}
