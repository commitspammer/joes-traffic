import Head from "next/head";
import styles from "../styles/home.module.scss";
import { Container } from "react-bootstrap";
import Map from "../components/Map";

export default function Home() {
  return (
    <div>
      <Map />
    </div>
  );
}
