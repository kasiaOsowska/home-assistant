import React from 'react';

const LibraryHome = () => {
  return (
    <div style={{ display: 'flex', justifyContent: 'center'}}>
      <div className='weather'style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', }}>
        <iframe width="650" height="410" src="https://embed.windy.com/embed.html?type=map&location=coordinates&metricRain=mm&metricTemp=°C&metricWind=m/s&zoom=5&overlay=radar&product=radar&level=surface&lat=51.319&lon=19.933&message=true" frameborder="0"></iframe>
        <iframe width="650" height="187" src="https://embed.windy.com/embed.html?type=forecast&location=coordinates&detail=true&detailLat=53.79470343648609&detailLon=20.437655137463764&metricTemp=°C&metricRain=mm&metricWind=m/s" frameborder="0"></iframe>
      </div>   
    </div>
  );
};

export default LibraryHome;
