import { useTheme } from '../../context/ThemeContext';
import { useAppConfig } from '../../context/AppConfigContext';
import { useSharedFavorites } from '../../context/FavoritesContext';
import { useSharedBLE } from '../../context/BLEContext';

export function useDockedState() {
  const theme = useTheme();
  const config = useAppConfig();
  const sharedFavorites = useSharedFavorites();
  const ble = useSharedBLE();

  return {
    Colors: theme.Colors,
    isDark: theme.isDark,
    isVisibilityAllowed: config.isVisibilityAllowed,
    ...sharedFavorites,
    getAdapterForDevice: ble.getAdapterForDevice,
  };
}
