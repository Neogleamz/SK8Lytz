import AsyncStorage from '@react-native-async-storage/async-storage';
import React, { createContext, useCallback, useContext, useEffect, useState } from 'react';
import { DarkColors, LightColors, ThemePalette } from '../theme/theme';
import { AppLogger } from '../services/AppLogger';

const THEME_KEY = '@Sk8lytz_ThemeMode';
const CONTROL_THEME_KEY = '@Sk8lytz_ControlUITheme';

interface ThemeContextType {
  Colors: ThemePalette;
  isDark: boolean;
  toggleTheme: () => void;
  controlUITheme: 'CLASSIC' | 'MODERN' | 'DOCKED';
  toggleControlUITheme: () => void;
}

const ThemeContext = createContext<ThemeContextType>({
  Colors: DarkColors,
  isDark: true,
  toggleTheme: () => {},
  controlUITheme: 'DOCKED',
  toggleControlUITheme: () => {},
});

export const ThemeProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [themeMode, setThemeMode] = useState<'dark' | 'light'>('dark');
  const [controlUITheme, setControlUITheme] = useState<'CLASSIC' | 'MODERN' | 'DOCKED'>('DOCKED');

  const isDark = themeMode === 'dark';

  useEffect(() => {
    AsyncStorage.getItem(THEME_KEY).then((val) => {
      if (val === 'dark' || val === 'light') setThemeMode(val);
    }).catch((err: unknown) => AppLogger.warn('[ThemeContext] THEME_KEY read failed', { error: err instanceof Error ? err.message : String(err), payload_size: 0, ssi: 0 }));
    AsyncStorage.getItem(CONTROL_THEME_KEY).then((val) => {
      if (val === 'CLASSIC' || val === 'MODERN' || val === 'DOCKED') setControlUITheme(val);
    }).catch((err: unknown) => AppLogger.warn('[ThemeContext] CONTROL_THEME_KEY read failed', { error: err instanceof Error ? err.message : String(err), payload_size: 0, ssi: 0 }));
  }, []);

  const toggleTheme = useCallback(() => {
    setThemeMode((prev) => {
      const next = prev === 'dark' ? 'light' : 'dark';
      AsyncStorage.setItem(THEME_KEY, next).catch((err: unknown) => AppLogger.warn('[ThemeContext] Failed to persist THEME_KEY', { error: err instanceof Error ? err.message : String(err), payload_size: 0, ssi: 0 }));
      return next;
    });
  }, []);

  const toggleControlUITheme = useCallback(() => {
    setControlUITheme((prev) => {
      const next = prev === 'CLASSIC' ? 'DOCKED' : 'CLASSIC';
      AsyncStorage.setItem(CONTROL_THEME_KEY, next).catch((err: unknown) => AppLogger.warn('[ThemeContext] Failed to persist CONTROL_THEME_KEY', { error: err instanceof Error ? err.message : String(err), payload_size: 0, ssi: 0 }));
      return next;
    });
  }, []);

  return (
    <ThemeContext.Provider value={{ 
      Colors: isDark ? DarkColors : LightColors, 
      isDark, 
      toggleTheme,
      controlUITheme,
      toggleControlUITheme 
    }}>
      {children}
    </ThemeContext.Provider>
  );
};

export const useTheme = () => useContext(ThemeContext);
