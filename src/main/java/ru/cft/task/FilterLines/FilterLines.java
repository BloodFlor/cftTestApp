package ru.cft.task.FilterLines;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class FilterLines {
	private boolean isNeedRewriteFile = true;
	private boolean isNeedStatistic = false;
	private boolean isNeedFullStatistic = false;

	private String prefixFile = "";
	private String pathFile = ".//";
	
	private ArrayList<String> fileArray = new ArrayList<String>();
	
    public void filter() {
		if (fileArray.size() > 0) {
			ArrayList<String>	stringArray		= new ArrayList<String>();
			ArrayList<String>	integerArray	= new ArrayList<String>();
			ArrayList<String>	floatArray		= new ArrayList<String>();
			
			Integer	minInteger = Integer.MAX_VALUE;
			Integer	maxInteger = -Integer.MAX_VALUE;
			Integer	sumInteger = 0;
			Float	minFloat = Float.MAX_VALUE;
			Float	maxFloat = -Float.MAX_VALUE;
			Float	sumFloat = .0f;
			Integer	countMinString = Integer.MAX_VALUE;
			Integer	countMaxString = 0;
			
			for(String fileName : fileArray) {
				try {
					File file = new File(fileName);
					Scanner myReader = new Scanner(file);

					while (myReader.hasNextLine()) {
						String data = myReader.nextLine().trim();

						if (!data.isEmpty()) {
							if (data.matches("^[+-]?\\d+$")) {
								try {
									integerArray.add(data);
									
									Integer tmpInteger = Integer.parseInt(data);
									if (isNeedFullStatistic) {
										if (minInteger > tmpInteger) {
											minInteger = tmpInteger;
										}
										if (maxInteger < tmpInteger) {
											maxInteger = tmpInteger;
										}
										sumInteger += tmpInteger;
									}
								} catch (NumberFormatException e) {
									System.out.println("Ошибка разбора числа \""+data+"\"");
								}
							} else if (data.matches("[+-]?\\d+\\.\\d+([eE][+-]?\\d+)?")) {
								Float tmpFloat = Float.parseFloat(data.replace(",","."));

								if (
									tmpFloat == Float.NaN 
									|| tmpFloat == Float.POSITIVE_INFINITY 
									|| tmpFloat == Float.NEGATIVE_INFINITY
								) {
									System.out.println("Ошибка разбора вещественногго числа \""+data+"\"");
								} else {
									floatArray.add(data);
									
									if (isNeedFullStatistic) {
										if (minFloat > tmpFloat) {
											minFloat = tmpFloat;
										}
										if (maxFloat < tmpFloat) {
											maxFloat = tmpFloat;
										}
										sumFloat += tmpFloat;
									}
								}
							} else {
								stringArray.add(data);
								
								if (isNeedFullStatistic) {
									Integer stringLength = data.length();
									if (countMinString > stringLength) {
										countMinString = stringLength;
									}
									if (countMaxString < stringLength) {
										countMaxString = stringLength;
									}
								}
							}
						}					
					}

					myReader.close();
				} catch (FileNotFoundException e) {
					System.out.println("Файл \""+fileName+"\" не найден или файл не доступен на чтение!");
					System.out.println("Данный файл будет пропущен.");
				}
			}

			if (integerArray.size() > 0) {
				writeDataInFile(
					integerArray,
					pathFile+prefixFile,
					"integers.txt",
					"	Минимальное значение: " + minInteger
					+ "\n	Максимальное значение: " + maxInteger
					+ "\n	Сумма: " + sumInteger
					+ "\n	Среднее значение: " + sumInteger / integerArray.size()
				);
			}
			
			if (floatArray.size() > 0) {
				writeDataInFile(
					floatArray,
					pathFile+prefixFile,
					"floats.txt",
					"	Минимальное значение: " + minFloat
					+ "\n	Максимальное значение: " + maxFloat
					+ "\n	Сумма: " + sumFloat
					+ "\n	Среднее значение: " + sumFloat / floatArray.size()
				);
			}
			
			if (stringArray.size() > 0) {
				writeDataInFile(
					stringArray,
					pathFile+prefixFile,
					"strings.txt",
					"	Размер самой короткой строки: " + countMinString
					+ "\n	Размер самой длинной строки: " + countMaxString
				);
			}
		} else {
			System.out.println("Нет файлов для разбора!");
		}
	}
	
	public void parseOptions(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if ("-a".equals(args[i])) {
				isNeedRewriteFile = false;
			} else if ("-s".equals(args[i])) {
				isNeedStatistic = true;
			} else if ("-f".equals(args[i])) {
				isNeedStatistic = true;
				isNeedFullStatistic = true;
			} else if ("-p".equals(args[i])) {
				if (i + 1 < args.length) {
					prefixFile = args[++i];
				} else {
					System.err.println("Пропущен путь для выходных файлов!");
					System.err.println("файлы будут созданы в текущей папке");
				}
			} else if ("-o".equals(args[i])) {
				if (i + 1 < args.length) {
					prefixFile = args[++i];
				} else {
					System.err.println("Пропущен префикс для выходных файлов!");
					System.err.println("файлы будут созданы без префикса.");
				}
			} else {
				addFile(args[i]);
			}	
		}
	}
	
	private void addFile(String fileName) {
		if (fileName.matches("^.*\\.(txt|TXT)$")) {
			fileArray.add(fileName);
		} else {
			System.err.println("Ключ не распознан или формат файла не поддерживается!");
			System.err.println("Данный параметр будет проигнорирован");
		}
	}

	private void writeDataInFile(ArrayList<String> dataArray, String pathFile, String nameFile, String fullStatisctic) {
		try
		{
			FileWriter newFile = new FileWriter(pathFile+nameFile, isNeedRewriteFile);
			for(String strng : dataArray) {
				newFile.write(strng+"\n");
			}
			newFile.close();
			
			if (isNeedStatistic) {
				System.out.println("Статистика по файлу \"floats.txt\":");
				System.out.println("	Записано "+dataArray.size()+" строк");
				
				if (isNeedFullStatistic) {
					System.out.println(fullStatisctic);
				}
			}
		}
		catch(IOException e){
			System.out.println("Ошибка создания\\перезаписи файла \""+nameFile+"\"!");
		}
    }
}