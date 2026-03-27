import React, { createContext, useContext, useState, useCallback, useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { DarkColors, LightColors, ThemePalette } from '../theme/theme';

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
  const [isDark, setIsDark] = useState(true);
  const [controlUITheme, setControlUITheme] = useState<'CLASSIC' | 'MODERN' | 'DOCKED'>('DOCKED');

  useEffect(() => {
    AsyncStorage.getItem(THEME_KEY).then((val) => {
      if (val !== null) setIsDark(val === 'dark');
    });
    AsyncStorage.getItem(CONTROL_THEME_KEY).then((val) => {
      if (val === 'CLASSIC' || val === 'MODERN' || val === 'DOCKED') setControlUITheme(val);
    });
  }, []);

  const toggleTheme = useCallback(() => {
    setIsDark((prev) => {
      const next = !prev;
      AsyncStorage.setItem(THEME_KEY, next ? 'dark' : 'light');
      return next;
    });
  }, []);

  const toggleControlUITheme = useCallback(() => {
    setControlUITheme((prev) => {
      const next = prev === 'CLASSIC' ? 'MODERN' : (prev === 'MODERN' ? 'DOCKED' : 'CLASSIC');
      AsyncStorage.setItem(CONTROL_THEME_KEY, next);
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
