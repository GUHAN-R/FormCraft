import style from "./Header.module.scss";

const Header = () => {
  return (
    <>
      <div className={style.container}>
        <div className={style.logo}>
          <a href="/">
            <h2 className={style.text}>Form Craft</h2>
          </a>
        </div>
      </div>
    </>
  );
};

export default Header;
