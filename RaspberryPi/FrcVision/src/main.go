/**
 * Created with IntelliJ IDEA.
 * User: Kian
 * Date: 10/5/13
 * Time: 10:33 AM
 * To change this template use File | Settings | File Templates.
 */
package main

import (
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	//"image/jpeg"
	"os"
)


func GrabImage(dst, src string, fin chan bool){
	res, err := http.Get(src)
	defer res.Body.Close()
	if err != nil {
		log.Fatal(err)
		fin<-false
	}

	imgraw, err := ioutil.ReadAll(res.Body)
	if err != nil {
		log.Fatal(err)
		fin<-false
	}

	err = ioutil.WriteFile(dst,imgraw,os.ModePerm)
	if err != nil {
		log.Fatal(err)
		fin<-false
	}

	fmt.Printf("finished writing \"%s\"\n",dst)
	fin<-true
}

func main() {
	src := "https://fbcdn-sphotos-b-a.akamaihd.net/hphotos-ak-prn1/329972_2490728718321_1082649794_o.jpg"
	dst := "garrisonHappy.jpg"

	fin := make(chan bool)

	go GrabImage(dst,src,fin)
	go GrabImage("robotc.png","http://www.robotc.net/w/images/b/b9/Robotc_textual_logo.png",fin)
	go GrabImage("googleCodeLogo.png","https://ssl.gstatic.com/codesite/ph/images/defaultlogo.png",fin)
	go GrabImage("loIps.jpg", "http://designer-daily.com/wp-content/uploads/2007/08/lorem-ipscream.jpg", fin)
	go GrabImage("jigaWatts.jpg", "http://www.thingaweak.com/wp-content/uploads/2008/09/jigawatts04.thumbnail.jpg",fin)

	for i:=0;i<5;i++{
		if(!<-fin){
			panic("Something went wrong!")
		}
	}
	fmt.Println("\nAll done.")
}
