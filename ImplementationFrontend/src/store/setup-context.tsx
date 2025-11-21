import React, { createContext, useContext, useState, ReactNode } from 'react';

interface SetupContextType {
  isInitialized: boolean;
  setInitialized: (initialized: boolean) => void;
}

const SetupContext = createContext<SetupContextType | undefined>(undefined);

export const SetupProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [isInitialized, setIsInitialized] = useState<boolean>(false);

  const setInitialized = (initialized: boolean) => {
    setIsInitialized(initialized);
  };

  return (
    <SetupContext.Provider value={{ isInitialized, setInitialized }}>
      {children}
    </SetupContext.Provider>
  );
};

export const useSetup = () => {
  const context = useContext(SetupContext);
  if (!context) {
    throw new Error('useSetup must be used within a SetupProvider');
  }
  return context;
};
