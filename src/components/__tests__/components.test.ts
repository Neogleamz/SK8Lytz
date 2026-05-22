import fs from 'fs';
import path from 'path';

describe('Component Import & Reference Static Checks', () => {
  const checkImports = (filePath: string) => {
    const content = fs.readFileSync(filePath, 'utf8');
    const importRegex = /import\s+.*?from\s+['"](\..*?)['"]/g;
    let match;
    const baseDir = path.dirname(filePath);

    while ((match = importRegex.exec(content)) !== null) {
      const relativePath = match[1];
      const absolutePath = path.resolve(baseDir, relativePath);

      // Check possible extensions
      const extensions = ['.ts', '.tsx', '.js', '.jsx', '/index.ts', '/index.tsx', '/index.js', '/index.jsx'];
      let found = fs.existsSync(absolutePath);
      if (!found) {
        for (const ext of extensions) {
          if (fs.existsSync(absolutePath + ext)) {
            found = true;
            break;
          }
          // Also handle case where extension is already in the file path but exists physically
          if (fs.existsSync(absolutePath)) {
            found = true;
            break;
          }
        }
      }

      if (!found) {
        throw new Error(`Failed to resolve import path "${relativePath}" from ${filePath}. Resolved target "${absolutePath}" does not exist.`);
      }
      
      expect(found).toBe(true);
    }
  };

  it('should have all valid relative imports in DockedController.tsx', () => {
    const file = path.resolve(__dirname, '../DockedController.tsx');
    checkImports(file);
  });

  it('should have all valid relative imports in DashboardScreen.tsx', () => {
    const file = path.resolve(__dirname, '../../screens/DashboardScreen.tsx');
    checkImports(file);
  });
});
