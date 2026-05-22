import React, { createContext, useContext } from 'react';
import { useFavorites as useFavoritesBase } from '../hooks/useFavorites';

type FavoritesContextType = ReturnType<typeof useFavoritesBase>;

export const FavoritesContext = createContext<FavoritesContextType | null>(null);

export const FavoritesProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const favoritesState = useFavoritesBase();
  return (
    <FavoritesContext.Provider value={favoritesState}>
      {children}
    </FavoritesContext.Provider>
  );
};

export function useSharedFavorites(): FavoritesContextType {
  const context = useContext(FavoritesContext);
  if (!context) {
    throw new Error('useSharedFavorites must be used within a FavoritesProvider');
  }
  return context;
}
