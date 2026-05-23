declare module "bun:sqlite" {
  export class Database {
    constructor(filename: string, options?: { create?: boolean; readOnly?: boolean });
    exec(sql: string): void;
    prepare(sql: string): {
      get(...params: any[]): any;
      all(...params: any[]): any[];
      run(...params: any[]): { lastInsertRowid: number | bigint; changes: number };
    };
    transaction(fn: any): any;
  }
}

declare module "node-fetch" {
  const fetch: any;
  export default fetch;
}
