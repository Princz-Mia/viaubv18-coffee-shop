import LoadingSpinner from './components/LoadingSpinner/LoadingSpinner';
import Header from './components/Header/Header';
import AppRoutes from './AppRoutes';
import { useLoading } from './hooks/useLoading';
import { setLoadingInterceptor } from './interceptors/loadingInterceptor';
import { useEffect, useRef } from 'react';
import Footer from './components/Footer/Footer';

function App() {
  const { showLoading, hideLoading } = useLoading();

  useEffect(() => {
    setLoadingInterceptor({ showLoading, hideLoading });
  }, [showLoading, hideLoading]);

  const homeRef = useRef(null);
  const aboutRef = useRef(null);
  const contactRef = useRef(null);


  return (
    <>
      <LoadingSpinner />
      <Header homeRef={homeRef} aboutRef={aboutRef} contactRef={contactRef} />
      <AppRoutes homeRef={homeRef} aboutRef={aboutRef} contactRef={contactRef} />
      <Footer homeRef={homeRef} aboutRef={aboutRef} contactRef={contactRef} />
    </>
  );
}

export default App;