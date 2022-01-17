$(function () {
  console.log("init");
  $('.coursesSlider').slick({
    arrows: false,
    infinite: false,
    slidesToShow: 4,
    slidesToScroll: 4,
    dots: false,
    responsive: [
      {
        breakpoint: 1024,
        settings: {
          slidesToShow: 3,
          slidesToScroll: 3,
          infinite: true,
          dots: true
        }
      },
      {
        breakpoint: 600,
        settings: {
          slidesToShow: 2,
          slidesToScroll: 2
        }
      },
      {
        breakpoint: 480,
        settings: {
          slidesToShow: 1,
          slidesToScroll: 1
        }
      }
      // You can unslick at a given breakpoint now by adding:
      // settings: "unslick"
      // instead of a settings object
    ]
  });

  $("#livePrev").click(() => {
    $("#liveCourseSlider").slick('slickPrev');
  })

  $("#liveNext").click(() => {
    $("#liveCourseSlider").slick('slickNext');
  })
  $("#recordPrev").click(() => {
    $("#recordedCourseSlider").slick('slickPrev');
  })

  $("#recordNext").click(() => {
    $("#recordedCourseSlider").slick('slickNext');
  })
});
